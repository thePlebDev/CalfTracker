package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke():Boolean{
        return authRepository.signUserOut()

    }
}