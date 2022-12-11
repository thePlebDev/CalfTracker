package com.elliottsoftware.calftracker.data.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepositoryImpl: LocationRepository {
    override suspend fun getLocation() = callbackFlow {
        trySend(Response.Loading)
        TODO("Not yet implemented")
    }
}