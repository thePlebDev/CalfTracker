package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.util.Actions
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun createUser(email:String, username:String): Flow<Response<Actions>>

    suspend fun createCalf(calf:FireBaseCalf):Flow<Response<Boolean>>

    suspend fun getCalves():Flow<Response<List<FireBaseCalf>>>

    suspend fun deleteCalf(id:String):Flow<Response<Boolean>>

    suspend fun updateCalf(fireBaseCalf: FireBaseCalf):Flow<Response<Boolean>>

}