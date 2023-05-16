package com.elliottsoftware.calftracker.data.repositories

import android.app.Activity
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.elliottsoftware.calftracker.background.BillingService
import com.elliottsoftware.calftracker.background.ServiceUtil
import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject


class BillingRepository @Inject constructor(
    private val externalScope: CoroutineScope
) {
    val isServiceBound = MutableStateFlow(false)
    private lateinit var mService: BillingService
    val mBound = MutableStateFlow(false)




    fun getServiceConnection() = object :ServiceConnection{
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as BillingService.LocalBinder
            mService =binder.getService()

            externalScope.launch {


                isServiceBound.emit(true)
                mBound.emit(true)

            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            externalScope.launch {

                isServiceBound.emit(false)
            }
        }

    }
    fun launchBillingFlow(
        activity:Activity,
        params: BillingFlowParams
    ){

        if(mBound.value){
            mService.launchFlow(
                activity,
                params
            )
        }else{

        }
    }

    fun fetchProductDetails(): Flow<Response<ProductDetails>> = flow {
        mBound.collect{ serviceConnected ->
            if(serviceConnected){
                mService.premiumProductDetails().collect{
                    emit(Response.Success(it))
                }

            }else{
                Response.Loading

            }

        }

    }

}