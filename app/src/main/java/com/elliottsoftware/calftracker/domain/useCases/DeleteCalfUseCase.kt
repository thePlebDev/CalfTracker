package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class DeleteCalfUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
):UseCase<String,Flow<Response<Boolean>>>() {


    override suspend fun execute(params: String): Flow<Response<Boolean>> {
        return databaseRepository.deleteCalf(params)
    }
}