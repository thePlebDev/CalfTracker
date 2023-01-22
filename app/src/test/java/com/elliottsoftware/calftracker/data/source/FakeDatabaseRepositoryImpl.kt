package com.elliottsoftware.calftracker.data.source

import com.elliottsoftware.calftracker.domain.models.DataPoint
import com.elliottsoftware.calftracker.domain.models.NetworkResponse
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.util.Actions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class FakeDatabaseRepositoryImpl:DatabaseRepository {
    private val calf:FireBaseCalf = FireBaseCalf("334","334","","","", Date(),"","")

    private val fullCalfList = listOf<FireBaseCalf>(calf,calf,calf)
    private val emptyCalfList = listOf<FireBaseCalf>()
    private var shouldReturnNetworkError = false
    private val flow = MutableSharedFlow<Response<List<FireBaseCalf>>>()
    override suspend fun createUser(email: String, username: String)= flow {
        //Flow<Response<Actions>>
        emit(Response.Loading)
    }

    override suspend fun createCalf(calf: FireBaseCalf) = flow{
        emit(Response.Loading)
         if(shouldReturnNetworkError){
            emit(Response.Success(true))
        }else{
            emit(Response.Failure(Exception("Exception")))
        }

    }

    override suspend fun getCalves()= flow {
        //Flow<Response<List<FireBaseCalf>>>
        emit(Response.Loading)
        if(shouldReturnNetworkError){
            emit(Response.Failure(Exception("Exception")))
        }else{
            emit(Response.Success(fullCalfList))
        }
    }

     override suspend fun getCalvesTest()= flow {
        //Flow<Response<List<FireBaseCalf>>>
        emit(NetworkResponse.Loading())
        if(shouldReturnNetworkError){
            emit(NetworkResponse.Error("PROBLEM"))
        }else{
            emit(NetworkResponse.Success(fullCalfList))
        }
    }

    override suspend fun deleteCalf(id: String)=flow {
        //Flow<Response<Boolean>>
        emit(Response.Loading)
    }

    override suspend fun updateCalf(fireBaseCalf: FireBaseCalf)=flow {
        //: Flow<Response<Boolean>>
        emit(Response.Loading)
    }

    override suspend fun getDataPoints()=flow {
        //: Flow<Response<List<DataPoint>>>
        emit(Response.Loading)
    }

    override suspend fun getCalvesByTagNumber(tagNumber: String)=flow {
        //: Flow<Response<List<FireBaseCalf>>>
        emit(Response.Loading)
    }
}