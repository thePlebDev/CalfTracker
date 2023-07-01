package com.elliottsoftware.calftracker.data.sources

import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface DatabaseSource {

    fun createUser(email: String, username: String): Flow<Response<Boolean>>
}