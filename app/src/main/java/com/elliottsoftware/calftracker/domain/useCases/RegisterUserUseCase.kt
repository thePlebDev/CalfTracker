package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.util.Actions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
data class RegisterUserParams(
    val email: String,
    val password: String,
)

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
):UseCase<RegisterUserParams,Flow<Response<Actions>>>() {


    override suspend fun execute(params: RegisterUserParams): Flow<Response<Actions>> {
        return authRepository.authRegister(params.email, params.password)
    }
}