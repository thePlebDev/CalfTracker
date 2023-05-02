package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaginatedCalfQuery @Inject constructor(
    val databaseRepository:DatabaseRepository
):UseCase<Long, Flow<Response<List<FireBaseCalf>>>>() {
    override suspend fun execute(queryLimit:Long): Flow<Response<List<FireBaseCalf>>> {

      return databaseRepository.getCalves(queryLimit)

    }

}


//class GetCalvesUseCase @Inject constructor(
//    val databaseRepository: DatabaseRepository
//):UseCase<Unit, Flow<Response<List<FireBaseCalf>>>>() {
//
//
//    override suspend fun execute(params: Unit): Flow<Response<List<FireBaseCalf>>> {
//        return databaseRepository.getCalves()
//    }
//}