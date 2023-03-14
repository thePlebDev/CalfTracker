package com.elliottsoftware.calftracker.presentation.components.subscription

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper

class BillingViewModel(application: Application): AndroidViewModel(application) {


    var billingClient: BillingClientWrapper = BillingClientWrapper(application)
    /************THIS REPOSITORY SETUP NEEDS TO BE LOOKED INTO***************/
//    private var repo: SubscriptionDataRepository =
//        SubscriptionDataRepository(billingClientWrapper = billingClient)
    
    private val _billingConnectionState = MutableLiveData(false)
    val billingConnectionState: LiveData<Boolean> = _billingConnectionState


    init {
        billingClient.startBillingConnection(billingConnectionState = _billingConnectionState)
    }
}