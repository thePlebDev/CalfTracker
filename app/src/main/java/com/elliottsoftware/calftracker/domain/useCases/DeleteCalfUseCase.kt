package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class DeleteCalfUseCase(
    val databaseRepository: DatabaseRepository = DatabaseRepositoryImpl()
) {
    suspend operator fun invoke(id:String): Flow<Response<Boolean>> {
        return databaseRepository.deleteCalf(id)

    }
}