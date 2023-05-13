package com.elliottsoftware.calftracker.data.repositories

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliottsoftware.calftracker.background.BillingService
import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber


class BillingRepository(
    private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob()) //when injected make this a singleton
) {

    private lateinit var mService: BillingService
     val mBound = MutableStateFlow(false)





    fun getServiceConnection() = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as BillingService.LocalBinder
            mService =binder.getService()

            externalScope.launch {

                mBound.emit(true)


            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            externalScope.launch {
                mBound.emit(false)

            }
        }
    }

     fun getStuff(): Flow<Int> = flow{
         mBound.collect{ serviceConnected ->

             if(serviceConnected){
                 emit(mService.randomNumber)

                 mService.premiumProductDetails().collect{
                     Timber.tag("meatballsP").d(it.toString())
                 }

             }else{
                 emit(999)
             }

         }
    }
    fun getMoreStuff(): Flow<Int> = flow{
            if(mBound.value){
                emit(mService.randomNumber)
            }else{
                emit(999)
            }
    }

}