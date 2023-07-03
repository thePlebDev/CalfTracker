package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



data class DeleteCalfParams(
    val calfId:String,
    val userEmail:String
)


class DeleteCalfUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
):UseCase<DeleteCalfParams,Flow<Response<Boolean>>>() {


    override suspend fun execute(params: DeleteCalfParams): Flow<Response<Boolean>> {
        return databaseRepository.deleteCalf(id = params.calfId, userEmail = params.userEmail)
    }
}