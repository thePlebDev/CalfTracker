package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class LoginParams(
    val email:String,
    val password:String,
)

class LoginUseCase @Inject constructor(
    private val authRepositoryImpl: AuthRepository
):UseCase<LoginParams,Flow<Response<Boolean>>>() {


    override suspend fun execute(params: LoginParams): Flow<Response<Boolean>> {
        return authRepositoryImpl.loginUser(params.email,params.password)
    }
}