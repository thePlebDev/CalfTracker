package com.elliottsoftware.calftracker.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliottsoftware.calftracker.domain.models.Response
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
class LocationManagerUtil private constructor() {

    companion object{
         fun setLocationClient(context: Context) = callbackFlow<Location>{
             var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            val item = fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                trySend(location)

            }

             awaitClose()
        }

    }


}

fun createLocationRequest() = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 3000).apply {

    setGranularity(Granularity.GRANULARITY_COARSE)
}.build()

// Send location updates to the consumer
@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow() = callbackFlow<Response<Location>> {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
           // result ?: return
            //THERE IS ONE LOCATION IN result.locations
            for (location in result.locations) {
                trySend(Response.Success(location)) // emit location into the Flow using ProducerScope.offer
            }
        }
    }
    requestLocationUpdates(
        createLocationRequest(),
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        close(e) // in case of error, close the Flow
    }

    awaitClose {
        removeLocationUpdates(callback) // clean up when Flow collection ends
    }
}