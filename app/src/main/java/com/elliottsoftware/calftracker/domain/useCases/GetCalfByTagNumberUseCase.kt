package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCalfByTagNumberUseCase@Inject constructor(
    val databaseRepository: DatabaseRepository
):UseCase<String,Flow<Response<List<FireBaseCalf>>>>() {

    override suspend fun execute(params: String): Flow<Response<List<FireBaseCalf>>> {




        return databaseRepository.getCalvesByTagNumber(params)
    }
}