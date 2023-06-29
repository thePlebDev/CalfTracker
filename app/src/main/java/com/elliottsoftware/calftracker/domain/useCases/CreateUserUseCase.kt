package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.util.Actions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class CreateUserParams(
    val email: String,
    val username: String,
)

class CreateUserUseCase @Inject constructor(
    private val database:AuthRepository
):UseCase<CreateUserParams,Flow<Response<Boolean>>>(){

    override suspend fun execute(params: CreateUserParams): Flow<Response<Boolean>> {
        return database.createUser(params.email,params.username)
    }


}

