package com.elliottsoftware.calftracker.background

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import java.util.*

class BillingService : Service() {
    // Binder given to clients.
    private val binder = LocalBinder()

    // Random number generator.
    private val mGenerator = Random()


    /** Method for clients.  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods

        fun getService(): BillingService = this@BillingService
    }





    //called by the Android system
    // IBinder gets passed to onServiceConnected()
    override fun onBind(intent: Intent): IBinder {

        return binder
    }
    companion object{
        private val connection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance.
                val binder = service as BillingService.LocalBinder
//            mService = binder.getService()
//            _uiState.value = true
            }

            override fun onServiceDisconnected(arg0: ComponentName) {

//            _uiState.value = false
            }
        }

        fun startService(context: Context) {

            Intent(context, BillingService::class.java).also { intent ->
                context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }
}