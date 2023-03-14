package com.elliottsoftware.calftracker.presentation.components.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class BillingClientWrapper(
    context: Context
) : PurchasesUpdatedListener,ProductDetailsResponseListener {

    // New Subscription ProductDetails
    private val _productWithProductDetails =
        MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
    val productWithProductDetails =
        _productWithProductDetails.asStateFlow()

    // Current Purchases
    private val _purchases =
        MutableStateFlow<List<Purchase>>(listOf())
    val purchases = _purchases.asStateFlow()

    // Tracks new purchases acknowledgement state.
    // Set to true when a purchase is acknowledged and false when not.
    private val _isNewPurchaseAcknowledged = MutableStateFlow(value = false)
    val isNewPurchaseAcknowledged = _isNewPurchaseAcknowledged.asStateFlow()


    // Initialize the BillingClient.
    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()



    override fun onPurchasesUpdated(
        billingResult: BillingResult, //contains the response code from the In-App billing API
        purchases: List<Purchase>? // a list of objects representing in-app purchases
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
            && !purchases.isNullOrEmpty()
        ) {
            // Post new purchase List to _purchases
            _purchases.value = purchases

            // Then, handle the purchases
            for (purchase in purchases) {
                acknowledgePurchases(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.

            Timber.tag("BILLINGR").e("User has cancelled")
        } else {
            // Handle any other error codes.
        }
    }
    private fun acknowledgePurchases(purchase: Purchase?) {
       // _isNewPurchaseAcknowledged's value is set to true when the acknowledgement is successfully processed.
        purchase?.let {
            if (!it.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(
                    params
                ) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        _isNewPurchaseAcknowledged.value = true //
                    }
                }
            }
        }
    }


    // Launch Purchase flow
    // THIS IS WHAT LAUNCHES THE GOOGLE PLAY PURCHASE SCREEN
    fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
        // BillingFlowParams object that contains the relevant ProductDetails object obtained from calling
        // queryProductDetailsAsync().
        if (!billingClient.isReady) {

            Timber.tag("BILLINGR").e("launchBillingFlow: BillingClient is not ready")
        }
        billingClient.launchBillingFlow(activity, params)

    }

    /*******CALLED TO INITIALIZE EVERYTHING******/
    // Establish a connection to Google Play.
    fun startBillingConnection(billingConnectionState: MutableLiveData<Boolean>) {

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Timber.tag("BILLINGR").d("Billing response OK")
                    // The BillingClient is ready. You can query purchases and product details here
                    queryPurchases()
                    queryProductDetails()
                    billingConnectionState.postValue(true)
                } else {

                    Timber.tag("BILLINGR").e(billingResult.debugMessage)
                }
            }

            override fun onBillingServiceDisconnected() {

                Timber.tag("BILLINGR").d("Billing connection disconnected")
                startBillingConnection(billingConnectionState)
            }
        })
    }

    // Query Google Play Billing for existing purchases.
    // New purchases will be provided to PurchasesUpdatedListener.onPurchasesUpdated().
    fun queryPurchases() {
        if (!billingClient.isReady) {

            Timber.tag("BILLINGR").e("queryPurchases: BillingClient is not ready")
        }

        // QUERY FOR EXISTING SUBSCRIPTION PRODUCTS THAT HAVE BEEN PURCHASED
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (!purchaseList.isNullOrEmpty()) {
                    _purchases.value = purchaseList
                } else {
                    _purchases.value = emptyList()
                }

            } else {
                Timber.tag("BILLINGR").e(billingResult.debugMessage)
            }
        }
    }

    /*****THE .setProductId(product) IS FOUND IN THE GOOGLE CONSOLE********/
    // Query Google Play Billing for products available to sell and present them in the UI
    fun queryProductDetails() {
        val params = QueryProductDetailsParams.newBuilder()
        val productList = mutableListOf<QueryProductDetailsParams.Product>()
        for (product in LIST_OF_PRODUCTS) {

            productList.add(
                QueryProductDetailsParams.Product.newBuilder() //This whole section is is just building a QueryProductDetailsParams.Product
                    .setProductId(product)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )
        }
        params.setProductList(productList).let { productDetailsParams ->

            Timber.tag("BILLINGR").d("queryProductDetailsAsync")
            billingClient.queryProductDetailsAsync(productDetailsParams.build(), this)
        }
    }
    companion object {
//        private const val TAG = "BillingClient"
//
//        // List of subscription product offerings
//        private const val BASIC_SUB = "up_basic_sub"
        private const val PREMIUM_SUB = "calf_tracker_premium_10"

        private val LIST_OF_PRODUCTS = listOf(PREMIUM_SUB)
    }

    override fun onProductDetailsResponse(
        billingResult: BillingResult,
        productDetailsList: MutableList<ProductDetails>
    ) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                var newMap = emptyMap<String, ProductDetails>()
                if (productDetailsList.isNullOrEmpty()) {

                    Timber.tag("BILLINGR").e("onProductDetailsResponse: Found null or empty ProductDetails. Check to see if the Products you requested are correctly published in the Google Play Console")

                } else {
                    newMap = productDetailsList.associateBy {
                        it.productId
                    }
                }
                _productWithProductDetails.value = newMap
            }
            else -> {

                Timber.tag("BILLINGR").i("onProductDetailsResponse: $responseCode $debugMessage")
            }
        }
    }

    fun terminateBillingConnection() {

        Timber.tag("BILLINGR").i("Terminating connection")
        billingClient.endConnection()
    }
}