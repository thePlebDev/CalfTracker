package com.elliottsoftware.calftracker.presentation.components.subscription

import android.app.Activity
import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.*
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.data.repositories.SubscriptionDataRepository
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.SubscriptionRepository
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

data class SubscriptionValues(
    val description:String,
    val title:String,
    val items:String,
    val price:String,
    val icon: ImageVector
)

data class BillingUiState(
    val subscriptionProduct: Response<ProductDetails> = Response.Loading, //THIS IS THE PRODUCT DETAILS
    val purchasedSubscriptions:Response<List<Purchase>> = Response.Loading, //THIS IS THE ACTUAL PRODUCTS
    val subscribed:Boolean = false,
    val subscribedInfo:SubscriptionValues = SubscriptionValues(
        description = "Loading...",
        title= "Loading...",
        items= "Loading...",
        price = "...",
        icon = Icons.Default.Autorenew
            ),
    val nextBillingPeriod:String =" None",
    val calfListSize:Int = 0

)

@HiltViewModel
class BillingViewModel @Inject constructor(
    application: Application,
    private val repo: SubscriptionRepository //Keep doing this
): AndroidViewModel(application),DefaultLifecycleObserver {

    private val _uiState = mutableStateOf(BillingUiState())
    val state = _uiState


    init {
        viewModelScope.launch {
            subscribedPurchases()

        }

    }
    init{
        refreshPurchases()
    }

    /**
     * We should be able to do a current collect that is found in all the other repository stuff
     * */
    init{
        Timber.tag("subscribedP").d("initialized")
        testingCollectingRepoInfo()
        viewModelScope.launch {
            getPurchases()

        }
    }

    /// TODO:  THIS NEEDS TO BE LOOKED INTO SOME MORE
    private fun testingCollectingRepoInfo() =viewModelScope.launch{

        repo.premiumProductDetails().collect{
            _uiState.value = _uiState.value.copy(
                subscriptionProduct = Response.Success(it)
            )
        }
    }
    fun updateCalfListSize(size:Int){
        _uiState.value = _uiState.value.copy(
            calfListSize = size
        )
    }

    private suspend fun getPurchases(){
        // Current purchases.
//         repo.purchases.collect{ purchases ->
//
//
//             _uiState.value = _uiState.value.copy(
//                 purchasedSubscriptions = Response.Success(purchases)
//             )
//         }

    }

    /**
     * THIS IS GETTING RUN ON THE onResume() LIFECYCLE CALL
     */
     fun refreshPurchases(){

        viewModelScope.launch {
            repo.queryPurchases()
            repo.hasRenewablePremium().collect { collectedSubscriptions ->

                    when(collectedSubscriptions){
                        is Response.Loading -> {
                            _uiState.value = _uiState.value.copy(

                                subscribedInfo = SubscriptionValues(
                                    description = "Fetching Subscription",
                                    title= "Loading...",
                                    items= "....",
                                    price = "....",
                                    icon = Icons.Default.Autorenew
                                )
                            )
                        }
                        is Response.Success ->{
                            if(collectedSubscriptions.data){
                                _uiState.value = _uiState.value.copy(

                                    subscribedInfo = SubscriptionValues(
                                        description = "Premium subscription",
                                        title= "Premium subscription",
                                        items= "Unlimited calf storage",
                                        price = "$10.00",
                                        icon = Icons.Default.MonetizationOn
                                    ),
                                    subscribed = true
                                )
                            }else{
                                _uiState.value = _uiState.value.copy(

                                    subscribedInfo = SubscriptionValues(
                                        description = "Basic subscription",
                                        title= "Basic subscription",
                                        items= "50 calf limit",
                                        price = "$0.00",
                                        icon = Icons.Default.CrueltyFree
                                    ),
                                    subscribed = false
                                )

                            }

                        }
                        is Response.Failure -> {}
                    }

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
            } // is a List<ProductDetails.SubscriptionOfferDetails>
//        Timber.tag("offers").d(offers?.size?.toString() ?: "NOTHING IN OFFERS")

        //offers is an empty list

        val offerToken = offers?.let { leastPricedOfferToken(it) } //offerToken gives us ""

//        Timber.tag("offers").d(offerToken ?: "NOTHING IN OFFER TOKENS")
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
//                billingClient.launchBillingFlow(
//                    activity,
//                    billingParams
//                )
                repo.launchBillingFlow(
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
//                billingClient.launchBillingFlow(
//                    activity,
//                    billingParams.build()
//                )
                repo.launchBillingFlow(
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
        repo.terminateConnection()
    }

    /******* I THINK I WANT TO PUT THIS AS A USECASE**********/
    // THIS SHOULD BE CLEANED UP A BIT INTO UTIL METHODS
    private fun subscribedPurchases(){

        viewModelScope.launch {
            repo.subscribedObject().collect{item ->
                when(val response = item){
                    is Response.Success ->{
                        val list = response.data

                        //todo: THE PURCHASES NEEDS TO BE CREATED INTO ITS OWN PRODUCT OBJECT FOR DETERMINING THE GRACE PERIOD
                        if(list.isNotEmpty()){
                            setDate(
                                purchase = list[0], numberOfDays = 30
                            )
                        }else{
                            _uiState.value = _uiState.value.copy(
                                nextBillingPeriod = "None"
                            )
                        }
                    }
                    is Response.Loading ->{
                        _uiState.value = _uiState.value.copy(
                            nextBillingPeriod = "None"
                        )
                    }
                    is Response.Failure ->{
                        _uiState.value = _uiState.value.copy(
                            nextBillingPeriod = "None"
                        )
                    }
                }

            }
        }



    }


    private fun setDate(purchase:Purchase,numberOfDays:Int){
        val timeStamp = purchase.purchaseTime
        val date = Date(timeStamp)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE,numberOfDays)

        _uiState.value = _uiState.value.copy(
            nextBillingPeriod = calendar.time.toString().subSequence(0,10).toString()
        )
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


    override fun onResume(owner: LifecycleOwner) {
//        Timber.tag("substuff").d("ONRESUME CALLED")
        refreshPurchases()
        subscribedPurchases()
    }

}