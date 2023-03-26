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

    override suspend fun createUser(email: String, username: String): Flow<Response<Actions>> {
        TODO("Not yet implemented")
    }

    override suspend fun createCalf(calf: FireBaseCalf): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCalves(): Flow<Response<List<FireBaseCalf>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCalvesTest(): Flow<NetworkResponse<List<FireBaseCalf>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCalf(id: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCalf(fireBaseCalf: FireBaseCalf): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCalvesByTagNumber(tagNumber: String): Flow<Response<List<FireBaseCalf>>> {
        TODO("Not yet implemented")
    }

}