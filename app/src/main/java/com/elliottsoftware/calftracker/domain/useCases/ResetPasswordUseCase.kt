package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    val authRepository: AuthRepository
):UseCase<String,Flow<Response<Boolean>>>() {

    override suspend fun execute(params: String): Flow<Response<Boolean>> {
        return authRepository.resetPassword(params)
    }
}