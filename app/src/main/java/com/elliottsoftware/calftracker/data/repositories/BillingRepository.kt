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

class BillingRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob())
) {

    private lateinit var mService: BillingService
    private val mBound = MutableStateFlow(false)
    val bound:StateFlow<Boolean> = mBound

    // I THINK I CAN SWITCH MutableLiveData WITH MutableStateFlow
// we always do checks against this


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

    suspend fun randomNumber():Int{
        delay(3000)
        if(mBound.value){
            return mService.randomNumber
        }else{
            return 999
        }


    }
    suspend fun getStuff(): Flow<Int> = flow{
        delay(3000)
        if(bound.value){
            emit(mService.randomNumber)
        }else{
            emit(999)
        }
    }



}
enum class ServiceState {
    CONNECTED, DISCONNECTED
}