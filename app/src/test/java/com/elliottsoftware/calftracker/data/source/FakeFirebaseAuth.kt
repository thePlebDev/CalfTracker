package com.elliottsoftware.calftracker.data.source

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow

class FakeFirebaseAuth(): AuthRepository {


     var shouldReturnNetworkError = false;


    override suspend fun authRegister(
        email: String,
        password: String
    ): Flow<SecondaryResponse<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(email: String, password: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun isUserSignedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun signUserOut(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }
}