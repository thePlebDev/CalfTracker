package com.elliottsoftware.calftracker.domain.repositories

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams)

    fun queryPurchases()
    fun terminateConnection()

    suspend fun premiumProductDetails():Flow<ProductDetails>

    suspend fun hasRenewablePremium():Flow<Response<Boolean>> // THIS IS WHAT IS USED TO DETERMINE IF THE USER IS SUBSCRIBED

    suspend fun subscribedObject():Flow<Response<List<Purchase>>>

    fun getBillingClient(): BillingClientWrapper



}