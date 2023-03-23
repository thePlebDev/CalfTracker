package com.elliottsoftware.calftracker.presentation.components.subscription

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.data.repositories.SubscriptionDataRepository
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfUIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber


data class BillingUiState(
    val subscriptionProduct: Response<ProductDetails> = Response.Loading,
    val purchasedSubscriptions:Response<List<Purchase>> = Response.Loading,
    val subscribed:Response<Boolean> = Response.Loading
)

class BillingViewModel(application: Application): AndroidViewModel(application){

    private val _uiState = mutableStateOf(BillingUiState())
    val state = _uiState


    var billingClient: BillingClientWrapper = BillingClientWrapper(application)
    /************THIS REPOSITORY SETUP NEEDS TO BE LOOKED INTO***************/
    private var repo: SubscriptionDataRepository =
        SubscriptionDataRepository(billingClientWrapper = billingClient)

    private val _billingConnectionState = MutableLiveData(false)
    val billingConnectionState: LiveData<Boolean> = _billingConnectionState

    /**
     * representing the various screens a user can be redirected to.
     */
//    private val _destinationScreen = MutableLiveData<DestinationScreen>()
//    val destinationScreen: LiveData<DestinationScreen> = _destinationScreen


    init {
        billingClient.startBillingConnection(billingConnectionState = _billingConnectionState)
    }
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

    init {
        viewModelScope.launch {
            /**
             * I think this is for if the user has already paid.
             * This can come later*/
            userCurrentSubscriptionFlow.collectLatest { collectedSubscriptions ->
                Timber.tag("BILLINGR").d("userCurrentSubscriptionFlow.collectLatest")
                Timber.tag("BILLINGR").d(collectedSubscriptions.hasRenewablePremium?.toString() ?: "Nothing on hasRenewablePremium")
                Timber.tag("BILLINGR").d(collectedSubscriptions.hasPrepaidPremium?.toString() ?: "Nothing on hasPrepaidPremium")
            }
        }
    }

    /**
     * We should be able to do a current collect that is found in all the other repository stuff
     * */
    init{
        testingCollectingRepoInfo()
        viewModelScope.launch {
            getPurchases()
        }
    }

    private fun testingCollectingRepoInfo() =viewModelScope.launch{
        repo.premiumProductDetails.collect{ it
            _uiState.value = _uiState.value.copy(
                subscriptionProduct = Response.Success(it)
            )

        }
    }
    private suspend fun getPurchases(){
        // Current purchases.
         repo.purchases.collect{ purchases ->
             val value = purchases.any { purchase: Purchase -> purchase.isAutoRenewing}
             _uiState.value = _uiState.value.copy(
                 purchasedSubscriptions = Response.Success(purchases)
             )
         }

    }

    /**
     * THIS IS GETTING RUN ON THE onResume() LIFECYCLE CALL
     */
     fun refreshPurchases(){

        viewModelScope.launch {
            billingClient.queryPurchases()
            repo.hasRenewablePremium.collect { collectedSubscriptions ->
                //val value = collectedSubscriptions.hasRenewablePremium
                _uiState.value = _uiState.value.copy(
                    subscribed = Response.Success(collectedSubscriptions)
                )
            }
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