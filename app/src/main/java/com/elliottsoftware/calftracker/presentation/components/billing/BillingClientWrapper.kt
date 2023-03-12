package com.elliottsoftware.calftracker.presentation.components.billing

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

    // Current Purchases
    private val _purchases =
        MutableStateFlow<List<Purchase>>(listOf())
    val purchases = _purchases.asStateFlow()

    // Initialize the BillingClient.
    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()



    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        TODO("Not yet implemented")
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
        // Query for existing subscription products that have been purchased.
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

    override fun onProductDetailsResponse(p0: BillingResult, p1: MutableList<ProductDetails>) {
        TODO("Not yet implemented")
    }
}