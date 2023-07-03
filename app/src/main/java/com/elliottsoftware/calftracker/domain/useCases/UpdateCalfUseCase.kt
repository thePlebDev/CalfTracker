package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



data class UpdateCalfParams(
    val calf:FireBaseCalf,
    val userEmail:String
)
class UpdateCalfUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
):UseCase<UpdateCalfParams,Flow<Response<Boolean>>>() {


    override suspend fun execute(params: UpdateCalfParams): Flow<Response<Boolean>> {
        return databaseRepository.updateCalf(
            fireBaseCalf = params.calf,
            userEmail = params.userEmail
        )
    }
}