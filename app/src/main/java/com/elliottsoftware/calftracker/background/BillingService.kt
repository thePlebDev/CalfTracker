package com.elliottsoftware.calftracker.background

import android.app.Service
import android.content.Intent
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
}