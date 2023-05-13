package com.elliottsoftware.calftracker.background

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.data.repositories.SubscriptionDataRepository
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BillingService: Service() {



    @Inject lateinit var billingClientWrapper: BillingClientWrapper
    val billingClientWrapperInitialized = MutableStateFlow(false)
    // Binder given to clients.
    private val binder = LocalBinder()

    // Random number generator.
    private val mGenerator = Random()



    /** Method for clients.  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

//     suspend fun subscribedObject()= flow {
//         delay(3000)
//        billingClientWrapper.purchases.map { value: List<Purchase> ->
//            val value = value.filter { purchase: Purchase ->  purchase.products.contains(
//                BillingService.PREMIUM_SUB
//            ) && purchase.isAutoRenewing}
//
//            Response.Success(value)
//        }.collect{
//
//            emit(it)
//        }
//    }


    val purchases = suspend {

        billingClientWrapperInitialized.emit(::billingClientWrapper.isInitialized)
        Timber.tag("meatballsP").d(::billingClientWrapper.isInitialized.toString() +" dELAY")

        billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.any { purchase: Purchase ->  purchase.products.contains(
                PREMIUM_SUB
            ) && purchase.isAutoRenewing}
            Response.Success(value)
        }
    }


    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods

        fun getService(): BillingService = this@BillingService
    }

    override fun onCreate() {

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag("meatballsP").d(::billingClientWrapper.isInitialized.toString() + "ON START COMMAND")

        return super.onStartCommand(intent, flags, startId)
    }


    //called by the Android system
    // IBinder gets passed to onServiceConnected()
    override fun onBind(intent: Intent): IBinder {

        return binder
    }


    suspend fun premiumProductDetails() = flow {

        Timber.tag("meatballsP").d("called")
        billingClientWrapper.productWithProductDetails.filter {
            Timber.tag("meatballsP").d(it.toString())

            it.containsKey(
                PREMIUM_SUB
            )
        }.map {

            val item = it[PREMIUM_SUB]!!
            Timber.tag("productsDetails").d(item.toString())
            item
        }.collect{
            emit(it)
        }

    }

    companion object {
        // List of subscription product offerings
        private const val PREMIUM_SUB = "calf_tracker_premium_10"

    }

}