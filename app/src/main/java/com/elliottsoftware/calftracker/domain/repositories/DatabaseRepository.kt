package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun createUser(email:String, username:String): Flow<SecondaryResponse<Boolean>>
}