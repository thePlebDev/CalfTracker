package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetCalvesUseCase(
    val databaseRepository: DatabaseRepository = DatabaseRepositoryImpl()
) {

    suspend operator fun invoke():Flow<Response<List<FireBaseCalf>>>{
        return databaseRepository.getCalves()
    }
}