package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) {
    operator fun invoke():Boolean{
        return authRepository.signUserOut()
    }
}