package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.util.Actions
import kotlinx.coroutines.flow.Flow
import com.elliottsoftware.calftracker.domain.models.DataPoint
import com.elliottsoftware.calftracker.domain.models.NetworkResponse


/**
 * A interface for interacting with a database. Methods in this interface support user/[FireBaseCalf] creation,
 * [FireBaseCalf] retrieval, [FireBaseCalf] deletion, [FireBaseCalf] updates and [DataPoint] retrieval;
 *
 *
 *
 */

interface DatabaseRepository {

    suspend fun createUser(email:String, username:String): Flow<Response<Actions>>

    suspend fun createCalf(calf:FireBaseCalf):Flow<Response<Boolean>>

    suspend fun getCalves():Flow<Response<List<FireBaseCalf>>>

    suspend fun getCalvesTest():Flow<NetworkResponse<List<FireBaseCalf>>>

    suspend fun deleteCalf(id:String):Flow<Response<Boolean>>

    suspend fun updateCalf(fireBaseCalf: FireBaseCalf):Flow<Response<Boolean>>


    suspend fun getCalvesByTagNumber(tagNumber:String):Flow<Response<List<FireBaseCalf>>>




}