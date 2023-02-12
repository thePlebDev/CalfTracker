package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class DeleteCalfParams(
    val id: String,
    val calfTag: String,
)

class DeleteCalfUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
):UseCase<DeleteCalfParams,Flow<Response<String>>>() {


    override suspend fun execute(params: DeleteCalfParams): Flow<Response<String>> {
        return databaseRepository.deleteCalf(params.id,params.calfTag)
    }
}