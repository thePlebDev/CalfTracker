package com.elliottsoftware.calftracker.domain.repositories

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams)

    suspend fun premiumProductDetails():Flow<ProductDetails>

}