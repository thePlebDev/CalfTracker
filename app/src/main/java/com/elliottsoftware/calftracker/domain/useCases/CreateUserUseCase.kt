package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class CreateUserUseCase(
    private val database:DatabaseRepository = DatabaseRepositoryImpl()
) {

    suspend operator fun invoke(email:String, username:String):Flow<SecondaryResponse<Boolean>>{
        return database.createUser(email, username)
    }
}