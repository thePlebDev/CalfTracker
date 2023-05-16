package com.elliottsoftware.calftracker.presentation.components.subscription

import android.app.Activity
import android.content.ServiceConnection
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.background.BillingService
import com.elliottsoftware.calftracker.data.repositories.BillingRepository
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import com.elliottsoftware.calftracker.presentation.viewModels.MainUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class SubscriptionUiState(
    val productDetails: Response<ProductDetails> = Response.Loading,
    val isUserSubscribed:Boolean = false
)

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    val billingRepository: BillingRepository
): ViewModel(), DefaultLifecycleObserver {



    private var _uiState: MutableState<SubscriptionUiState> = mutableStateOf(SubscriptionUiState())
    val state:State<SubscriptionUiState> = _uiState


    init{
        getProductDetails()
    }
    init{
        isUserSubscribed()
    }

    fun serviceConnection():ServiceConnection{
        return billingRepository.getServiceConnection()
    }
    fun isUserSubscribed() = viewModelScope.launch{
        billingRepository.isUserSubscribed().collect{ response ->
            _uiState.value = _uiState.value.copy(
                isUserSubscribed = response
            )


        }
    }

    /**
     * Used to get the product details for our specified virtual product, calf_tracker_premium_10
     *
     */
    fun getProductDetails() = viewModelScope.launch(Dispatchers.IO) {
        billingRepository.fetchProductDetails().collect{ response ->
            _uiState.value = _uiState.value.copy(
                productDetails = response
            )
        }
    }

    /**
     * Use the Google Play Billing Library to make a purchase.
     *
     * @param productDetails ProductDetails object returned by the library.
     * @param currentPurchases List of current [Purchase] objects needed for upgrades or downgrades.
     * @param billingClient Instance of [BillingClientWrapper].
     * @param activity [Activity] instance.
     * @param tag String representing tags associated with offers and base plans.
     */
    fun buy(
        productDetails: ProductDetails,
        currentPurchases: List<Purchase>?,
        activity: Activity,
        tag: String
    ) {

        val offers =
            productDetails.subscriptionOfferDetails?.let {
                retrieveEligibleOffers(
                    offerDetails = it,
                    tag = tag.lowercase()
                )
            }

        val offerToken = offers?.let { leastPricedOfferToken(it) } //offerToken gives us ""


        val oldPurchaseToken: String

        // THIS WONT RUN
        if (!currentPurchases.isNullOrEmpty() &&
            currentPurchases.size == MAX_CURRENT_PURCHASES_ALLOWED
        ) {
            // This either an upgrade, downgrade, or conversion purchase.
            // This is nothing for us
            val currentPurchase = currentPurchases.first()

            // Get the token from current purchase.
            //nothing for us
            oldPurchaseToken = currentPurchase.purchaseToken


            val billingParams = offerToken?.let {
                upDowngradeBillingFlowParamsBuilder(
                    productDetails = productDetails, //null
                    offerToken = it, // ""
                    oldToken = oldPurchaseToken
                )
            }

            if (billingParams != null) {

                billingRepository.launchBillingFlow(
                    activity,
                    billingParams
                )
            }
            /************THIS GET RUNS************/
        } else if (currentPurchases == null) {


            // This is not null
            val billingParams = offerToken?.let {
                billingFlowParamsBuilder(
                    productDetails = productDetails,
                    offerToken = it
                )
            }


            if (billingParams != null) {
                billingRepository.launchBillingFlow(
                    activity,
                    billingParams.build()
                )

            }
        } else if (!currentPurchases.isNullOrEmpty() &&
            currentPurchases.size > MAX_CURRENT_PURCHASES_ALLOWED
        ) {
            // The developer has allowed users  to have more than 1 purchase, so they need to
            /// implement a logic to find which one to use.


        }
    }




    /**
     * This method helps retrieve all offers and base plans a user is eligible for
     * by using the newly introduced concept of tags that are used to group related offers.
     * */
    private fun retrieveEligibleOffers(
        offerDetails: MutableList<ProductDetails.SubscriptionOfferDetails>,
        tag: String
    ): List<ProductDetails.SubscriptionOfferDetails> {
        val eligibleOffers = emptyList<ProductDetails.SubscriptionOfferDetails>().toMutableList()
        offerDetails.forEach { offerDetail ->
            if (offerDetail.offerTags.contains(tag)) {
                eligibleOffers.add(offerDetail)
            }
        }

        return eligibleOffers
    }
    /**
     * When a user is eligible for multiple offers, the leastPricedOfferToken() method
     * is used to calculate the lowest offer amongst the ones returned by retrieveEligibleOffers().
     * */
    private fun leastPricedOfferToken(
        offerDetails: List<ProductDetails.SubscriptionOfferDetails>
    ): String {
        var offerToken = String()
        var leastPricedOffer: ProductDetails.SubscriptionOfferDetails
        var lowestPrice = Int.MAX_VALUE

        if (!offerDetails.isNullOrEmpty()) {
            for (offer in offerDetails) {
                for (price in offer.pricingPhases.pricingPhaseList) {
                    if (price.priceAmountMicros < lowestPrice) {
                        lowestPrice = price.priceAmountMicros.toInt()
                        leastPricedOffer = offer
                        offerToken = leastPricedOffer.offerToken
                    }
                }
            }
        }
        return offerToken
    }

    /**
     * builds the params for normal purchases.
     * */
    private fun billingFlowParamsBuilder(
        productDetails: ProductDetails,
        offerToken: String
    ): BillingFlowParams.Builder {

        return BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        )
    }


    /**
     *builds the params for upgrades and downgrades
     * */
    private fun upDowngradeBillingFlowParamsBuilder(
        productDetails: ProductDetails,
        offerToken: String,
        oldToken: String
    ): BillingFlowParams {
        return BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        ).setSubscriptionUpdateParams(
            BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                .setOldPurchaseToken(oldToken)
                .setReplaceProrationMode(
                    BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_FULL_PRICE
                )
                .build()
        ).build()
    }
    override fun onResume(owner: LifecycleOwner) {
       Timber.tag("ANOTHERSONE").d("SubscriptionViewModel ONRESUME CALLED")
        isUserSubscribed()

    }

    companion object {


        private const val MAX_CURRENT_PURCHASES_ALLOWED = 1
    }

}