package com.elliottsoftware.calftracker.data.repositories

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.SubscriptionRepository
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import kotlinx.coroutines.flow.*
import timber.log.Timber
import kotlinx.coroutines.flow.flow

class SubscriptionDataRepository(
    private val billingClientWrapper: BillingClientWrapper
    ): SubscriptionRepository {



    // Set to true when a returned purchase is an auto-renewing basic subscription.
    /**IS HOW WE WILL DETERMINE IF THERE IS A SUBSCRIPTION OR NOT*/
    //
    val hasRenewablePremium: Flow<Response<Boolean>> = try{
        billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.any { purchase: Purchase ->  purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing}
            Response.Success(value)
        }
    }catch (e:Exception){

        MutableStateFlow(Response.Failure(e))
    }

    val subscribedObject: Flow<Response<List<Purchase>>> = try{


        billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.filter { purchase: Purchase ->  purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing}
            Response.Success(value)
        }
    }catch (e:Exception){

        MutableStateFlow(Response.Failure(e))
    }
    // Set to true when a returned purchase is prepaid premium subscription.
    val hasPrepaidPremium: Flow<Boolean> = billingClientWrapper.purchases.map { purchaseList ->
        purchaseList.any { purchase ->
            !purchase.isAutoRenewing && purchase.products.contains(PREMIUM_SUB)
        }
    }



    /**
     * THIS SHOULD PROBABLY HAVE A TRY CATCH BLOCK*/
    // ProductDetails for the premium subscription.
    val premiumProductDetails: Flow<ProductDetails> =
        billingClientWrapper.productWithProductDetails.filter {

            it.containsKey(
                PREMIUM_SUB
            )
        }.map {

           val item = it[PREMIUM_SUB]!!
            Timber.tag("productsDetails").d(item.toString())
            item
        }

    //terminate the connection
    override fun terminateConnection(){
       // billingClientWrapper.terminateBillingConnection()
    }
    override fun launchBillingFlow(activity: Activity, params: BillingFlowParams){
//        billingClientWrapper.launchBillingFlow(
//            activity,
//            params
//        )
    }


    /**
     * Will run in the main thread and has a main goal of
     * returning a flow containing a
     * [ProductDetails](https://developer.android.com/reference/com/android/billingclient/api/ProductDetails)
     * object, which is needed for the
     * [buy][com.elliottsoftware.calftracker.presentation.components.subscription.BillingViewModel.buy] method
     *
     *
     */
    override suspend fun premiumProductDetails() = flow {

        billingClientWrapper.productWithProductDetails.filter {

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

    override suspend fun hasRenewablePremium() = flow  {
         billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.any { purchase: Purchase ->  purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing}
            Response.Success(value)
        }.collect{
             emit(it)
         }

    }

    override suspend fun subscribedObject()=flow {
        billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.filter { purchase: Purchase ->  purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing}
            Response.Success(value)
        }.collect{
            emit(it)
        }
    }

    override fun getBillingClient(): BillingClientWrapper {
        return billingClientWrapper
    }

    override fun queryPurchases(){
        billingClientWrapper.queryPurchases()
    }



    // List of current purchases returned by the Google PLay Billing client library.
    val purchases: Flow<List<Purchase>> = billingClientWrapper.purchases

    // Set to true when a purchase is acknowledged.
    val isNewPurchaseAcknowledged: Flow<Boolean> = billingClientWrapper.isNewPurchaseAcknowledged


    companion object {
        // List of subscription product offerings
        private const val PREMIUM_SUB = "calf_tracker_premium_10"

    }


}
