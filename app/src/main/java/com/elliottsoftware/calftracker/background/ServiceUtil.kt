package com.elliottsoftware.calftracker.background

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class ServiceUtil(
    private val externalScope: CoroutineScope
) : ServiceConnection {

    private lateinit var mService: BillingService
    val isServiceBound = MutableStateFlow(false)
    val mBound = MutableStateFlow(false)
    val outBound = MutableStateFlow(false)

    override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
        val binder = service as BillingService.LocalBinder
        mService =binder.getService()

        externalScope.launch {


            isServiceBound.emit(true)
            mBound.emit(true)
           // Timber.tag("THINGERS").d("ServiceUtil CONNECTED ${isServiceBound.value}")


        }
    }

    init{
        externalScope.launch{
            getStuff().collect{

            }
        }

    }



    fun getStuff(): Flow<Boolean> = channelFlow{

            mBound.collect{ serviceConnected ->
                if(serviceConnected){
                    Timber.tag("THINGERS").d("getStuff ${mBound.value}")
                    send(true)

                }else{
                    Timber.tag("THINGERS").d("getStuff ${mBound.value}")
                    send(false)
                }

            }


    }

    fun observeService() = flow{
        isServiceBound.collect{
            delay(3000)
            if(it){
                emit(it)
                Timber.tag("THINGERS").d("observeService ${it}")

            }else{
                emit(it)
                Timber.tag("THINGERS").d("observeService ${it}")
            }
        }
    }



    override fun onServiceDisconnected(p0: ComponentName?) {
        externalScope.launch {
            Timber.tag("THINGERS").d("ServiceUtil DISSCONNECT")
            isServiceBound.emit(false)
        }
    }
}