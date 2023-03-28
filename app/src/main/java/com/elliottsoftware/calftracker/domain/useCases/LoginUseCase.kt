package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.presentation.components.login.Credentials
import com.elliottsoftware.calftracker.presentation.components.login.LoginResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



/**
 * A concrete implementation of [UseCase] that will request logging in via [AuthRepository]*/
class LoginUseCase @Inject constructor(
    private val authRepositoryImpl: AuthRepository
):UseCase<Credentials,Flow<Response<Boolean>>>() {


    override suspend fun execute(params: Credentials): Flow<Response<Boolean>> {

        return authRepositoryImpl.loginUser(params.email.value,params.password.value)
    }
}