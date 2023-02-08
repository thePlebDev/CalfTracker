package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCalvesUseCase @Inject constructor(
    val databaseRepository: DatabaseRepository
):UseCase<Unit,Flow<Response<List<FireBaseCalf>>>>() {



    suspend fun getCalfByTagNumber(tagNumber:String):Flow<Response<List<FireBaseCalf>>>{
        return databaseRepository.getCalvesByTagNumber(tagNumber)
    }

    override suspend fun execute(params: Unit): Flow<Response<List<FireBaseCalf>>> {
        return databaseRepository.getCalves()
    }
}