package com.elliottsoftware.calftracker.presentation

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.animation.AnticipateInterpolator

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.elliottsoftware.calftracker.BuildConfig
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.logging.CrashAndLog
import com.elliottsoftware.calftracker.logging.CrashReportingTree
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.*
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.elliottsoftware.calftracker.background.BillingService
import com.elliottsoftware.calftracker.background.ServiceUtil
import com.elliottsoftware.calftracker.data.repositories.BillingRepository
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber.Forest.plant
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val subscriptionViewModel: SubscriptionViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        // Bind to LocalService.


    }

    override fun onStop() {
        super.onStop()


    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(subscriptionViewModel.serviceConnection())
        Timber.tag("CLOSINGT").d("ACTIVITY DESTROYED")
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Intent(this, BillingService::class.java).also { intent ->
            bindService(intent, subscriptionViewModel.serviceConnection(), Context.BIND_AUTO_CREATE)
        }
        supportActionBar!!.hide()
       setContentView(R.layout.activity_main)



        //for debugging
        CrashAndLog.setupTimber()

    }


}