package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import javax.inject.Inject

class CheckUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
):UseCase<Unit,Boolean>(){

    override suspend fun execute(params: Unit): Boolean {
        return authRepository.isUserSignedIn();
    }


}

