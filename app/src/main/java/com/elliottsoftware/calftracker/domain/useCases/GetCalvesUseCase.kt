package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


data class GetCalvesParams(
    val calfLimit:Long,
    val userEmail: String
)
class GetCalvesUseCase @Inject constructor(
    val databaseRepository: DatabaseRepository
):UseCase<GetCalvesParams,Flow<Response<List<FireBaseCalf>>>>() {


    override suspend fun execute(params: GetCalvesParams): Flow<Response<List<FireBaseCalf>>> {
        return databaseRepository.getCalves(params.calfLimit,params.userEmail)
    }


}