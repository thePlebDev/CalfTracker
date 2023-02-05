package com.elliottsoftware.calftracker.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import timber.log.Timber


/**
 * Find the closest Activity in a given Context.
 */
internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        Timber.tag("CONTEXTS").d(context.toString())
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

/**
 * Find the closest Activity in a given Context.
 */
internal fun Context.loopContext() {
    var context = this

    while (context is ContextWrapper) {
        Log.d("CONTEXTSTUFF",context.toString())
        context = context.baseContext
    }
    Log.d("CONTEXTSTUFF",context.toString())
//    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
