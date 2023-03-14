package com.elliottsoftware.calftracker.presentation.components.subscription

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.data.repositories.SubscriptionDataRepository
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

class BillingViewModel(application: Application): AndroidViewModel(application) {


    var billingClient: BillingClientWrapper = BillingClientWrapper(application)
    /************THIS REPOSITORY SETUP NEEDS TO BE LOOKED INTO***************/
    private var repo: SubscriptionDataRepository =
        SubscriptionDataRepository(billingClientWrapper = billingClient)

    private val _billingConnectionState = MutableLiveData(false)
    val billingConnectionState: LiveData<Boolean> = _billingConnectionState

    /**
     * representing the various screens a user can be redirected to.
     */
    private val _destinationScreen = MutableLiveData<DestinationScreen>()
    val destinationScreen: LiveData<DestinationScreen> = _destinationScreen


    init {
        billingClient.startBillingConnection(billingConnectionState = _billingConnectionState)
    }
    init {
        viewModelScope.launch {
            userCurrentSubscriptionFlow.collectLatest { collectedSubscriptions ->
                when {
                    collectedSubscriptions.hasRenewableBasic == true &&
                            collectedSubscriptions.hasRenewablePremium == false -> {
                        _destinationScreen.postValue(DestinationScreen.BASIC_RENEWABLE_PROFILE)
                    }
                    collectedSubscriptions.hasRenewablePremium == true &&
                            collectedSubscriptions.hasRenewableBasic == false -> {
                        _destinationScreen.postValue(DestinationScreen.PREMIUM_RENEWABLE_PROFILE)
                    }
                    collectedSubscriptions.hasPrepaidBasic == true &&
                            collectedSubscriptions.hasPrepaidPremium == false -> {
                        _destinationScreen.postValue(DestinationScreen.BASIC_PREPAID_PROFILE_SCREEN)
                    }
                    collectedSubscriptions.hasPrepaidPremium == true &&
                            collectedSubscriptions.hasPrepaidBasic == false -> {
                        _destinationScreen.postValue(
                            DestinationScreen.PREMIUM_PREPAID_PROFILE_SCREEN
                        )
                    }
                    else -> {
                        _destinationScreen.postValue(DestinationScreen.SUBSCRIPTIONS_OPTIONS_SCREEN)
                    }
                }
            }

        }
    }
    val productsForSaleFlows = combine(
        flow =repo.basicProductDetails,
        flow2 = repo.premiumProductDetails,
        transform = {
                basicProductDetails,
                premiumProductDetails
            ->
            MainState(
                premiumProductDetails = premiumProductDetails,
                basicProductDetails = basicProductDetails
            )
        }
    )

    // The userCurrentSubscriptionFlow object combines all the possible subscription flows into one
// for emission.
    private val userCurrentSubscriptionFlow = combine(
        repo.hasRenewablePremium,
        repo.hasPrepaidPremium
    ){
        hasRenewablePremium,
        hasPrepaidPremium
            ->
        MainState(
            hasRenewablePremium = hasRenewablePremium,
            hasPrepaidPremium = hasPrepaidPremium
        )

    }

    // Current purchases.
    val currentPurchasesFlow = repo.purchases

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
        val offerToken = offers?.let { leastPricedOfferToken(it) }
        val oldPurchaseToken: String

        // Get current purchase. In this app, a user can only have one current purchase at
        // any given time.
        if (!currentPurchases.isNullOrEmpty() &&
            currentPurchases.size == MAX_CURRENT_PURCHASES_ALLOWED
        ) {
            // This either an upgrade, downgrade, or conversion purchase.
            val currentPurchase = currentPurchases.first()

            // Get the token from current purchase.
            oldPurchaseToken = currentPurchase.purchaseToken

            val billingParams = offerToken?.let {
                upDowngradeBillingFlowParamsBuilder(
                    productDetails = productDetails,
                    offerToken = it,
                    oldToken = oldPurchaseToken
                )
            }

            if (billingParams != null) {
                billingClient.launchBillingFlow(
                    activity,
                    billingParams
                )
            }
        } else if (currentPurchases == null) {
            // This is a normal purchase.
            val billingParams = offerToken?.let {
                billingFlowParamsBuilder(
                    productDetails = productDetails,
                    offerToken = it
                )
            }

            if (billingParams != null) {
                billingClient.launchBillingFlow(
                    activity,
                    billingParams.build()
                )
            }
        } else if (!currentPurchases.isNullOrEmpty() &&
            currentPurchases.size > MAX_CURRENT_PURCHASES_ALLOWED
        ) {
            // The developer has allowed users  to have more than 1 purchase, so they need to
            /// implement a logic to find which one to use.

            Timber.tag("BILLINGR").d("User has more than 1 current purchase.")
        }
    }


    // When an activity is destroyed the viewModel's onCleared is called, so we terminate the
    // billing connection.
    override fun onCleared() {
        billingClient.terminateBillingConnection()
    }


    /**
     * Enum representing the various screens a user can be redirected to.
     */
    enum class DestinationScreen {
        SUBSCRIPTIONS_OPTIONS_SCREEN,
        BASIC_PREPAID_PROFILE_SCREEN,
        BASIC_RENEWABLE_PROFILE,
        PREMIUM_PREPAID_PROFILE_SCREEN,
        PREMIUM_RENEWABLE_PROFILE;
    }

    companion object {


        private const val MAX_CURRENT_PURCHASES_ALLOWED = 1
    }

}