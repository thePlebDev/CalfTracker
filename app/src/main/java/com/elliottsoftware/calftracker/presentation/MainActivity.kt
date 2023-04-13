package com.elliottsoftware.calftracker.presentation

import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Build
import android.os.Bundle
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
import timber.log.Timber.Forest.plant


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
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
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

        //for debugging
        CrashAndLog.setupTimber()



    }
}