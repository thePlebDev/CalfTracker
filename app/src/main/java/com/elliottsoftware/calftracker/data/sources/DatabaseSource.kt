package com.elliottsoftware.calftracker.data.sources

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import kotlinx.coroutines.flow.Flow

interface DatabaseSource {

    fun createUser(email: String, username: String): Flow<Response<Boolean>>

    fun createCalf(
        calf: FireBaseCalf,
        userEmail: String
    ): Flow<Response<Boolean>>

    fun getCalves(queryLimit:Long,userEmail: String): Flow<Response<List<FireBaseCalf>>>
}