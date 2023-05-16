package com.elliottsoftware.calftracker.background

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.data.repositories.SubscriptionDataRepository
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


/**
 * A group of **members**.
 *
 * This class represents a Bound Service and is used to talk to [BillingClientWrapper][com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper]
 *
 */
@AndroidEntryPoint
class BillingService: Service() {




    @Inject lateinit var billingClientWrapper: BillingClientWrapper // I THINK THIS GETS INJECTED IN THE Service#onCreate()
    val billingClientWrapperInitialized = MutableStateFlow(false)
    // Binder given to clients.
    private val binder = LocalBinder()



    // Random number generator.
    private val mGenerator = Random()





    override fun onCreate() {
        super.onCreate()
        billingClientWrapper.startBillingConnection(MutableLiveData(false))


    }

    override fun onDestroy() {
        // The service is no longer used and is being destroyed
        super.onDestroy()

    }



//


    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods

        fun getService(): BillingService = this@BillingService
    }



    //called by the Android system
    // IBinder gets passed to onServiceConnected()
    override fun onBind(intent: Intent): IBinder {

        return binder
    }



    /**
     * - Called to fetch the [ProductDetails](https://developer.android.com/reference/com/android/billingclient/api/ProductDetails)
     * from our [BillingClientWrapper][com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper]
     *
     *  @return Flow<ProductDetails>
     */
    suspend fun premiumProductDetails() = flow {
        billingClientWrapper.productWithProductDetails.filter {
            it.containsKey(
                PREMIUM_SUB
            )
        }.map {

            val item = it[PREMIUM_SUB]!!

            item
        }.collect{
            emit(it)
        }

    }
    fun launchFlow(
        activity:Activity,
        params: BillingFlowParams
    ){
        Timber.tag("billingClientWrapper").d("billingClientWrapper.launchBillingFlow()")
        billingClientWrapper.launchBillingFlow(
            activity,
            params
        )
    }

    // Set to true when a returned purchase is an auto-renewing basic subscription.
    /**IS HOW WE WILL DETERMINE IF THERE IS A SUBSCRIPTION OR NOT*/
    //
    /**
     * - Called to determine if the user is subscribed. Should be run on the onResume() methods
     * of the Fragment or activity
     */
    val hasRenewablePremium: Flow<Response<Boolean>> = try{
        billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.any { purchase: Purchase ->  purchase.products.contains(
                PREMIUM_SUB
            ) && purchase.isAutoRenewing}
            Response.Success(value)
        }
    }catch (e:Exception){

        MutableStateFlow(Response.Failure(e))
    }


    companion object {
        // List of subscription product offerings
        private const val PREMIUM_SUB = "calf_tracker_premium_10"

    }

}