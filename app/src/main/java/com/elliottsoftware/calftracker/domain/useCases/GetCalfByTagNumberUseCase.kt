package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject



data class GetCalfByTagNumberParams(
    val tagNumber:String,
    val userEmail:String
)
class GetCalfByTagNumberUseCase@Inject constructor(
    val databaseRepository: DatabaseRepository
):UseCase<GetCalfByTagNumberParams,Flow<Response<List<FireBaseCalf>>>>() {

    override suspend fun execute(params: GetCalfByTagNumberParams): Flow<Response<List<FireBaseCalf>>> {


        return databaseRepository.getCalvesByTagNumber(
            tagNumber = params.tagNumber,
            userEmail = params.userEmail
        )
    }
}