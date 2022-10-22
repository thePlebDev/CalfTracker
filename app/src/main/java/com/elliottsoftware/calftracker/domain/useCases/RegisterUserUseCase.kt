package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow

class RegisterUserUseCase(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) {
    suspend operator fun invoke(email:String, password:String):Flow<SecondaryResponse<Boolean>>{
        return authRepository.authRegister(email, password)

    }
}