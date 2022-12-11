package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLocation(): Flow<Response<List<Double>>>
}