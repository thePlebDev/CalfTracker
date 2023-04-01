package com.elliottsoftware.calftracker.data.repositories

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import kotlinx.coroutines.flow.*
import timber.log.Timber

class SubscriptionDataRepository(billingClientWrapper: BillingClientWrapper) {



    // Set to true when a returned purchase is an auto-renewing basic subscription.
    /**IS HOW WE WILL DETERMINE IF THERE IS A SUBSCRIPTION OR NOT*/
    val hasRenewablePremium: Flow<Response<Boolean>> = try{
        billingClientWrapper.purchases.map { value: List<Purchase> ->
            val value = value.any { purchase: Purchase ->  purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing}
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

            it[PREMIUM_SUB]!!
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