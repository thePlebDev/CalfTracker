package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow

class ResetPasswordUseCase(
    val authRepository: AuthRepository = AuthRepositoryImpl()
) {

    suspend operator fun invoke(email:String): Flow<Response<Boolean>> {
        return authRepository.resetPassword(email)
    }
}