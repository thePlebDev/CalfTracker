package com.elliottsoftware.calftracker.presentation.components.subscription

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase

data class MainState(
    val hasRenewableBasic: Boolean? = false,
    val hasPrepaidBasic: Boolean? = false,
    val hasRenewablePremium: Boolean? = false,
    val hasPrepaidPremium: Boolean? = false,
    val basicProductDetails: ProductDetails? = null,
    val premiumProductDetails: ProductDetails? = null,
    val purchases: List<Purchase>? = null,
)
