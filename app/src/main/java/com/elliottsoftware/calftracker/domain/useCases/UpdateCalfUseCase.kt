package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UpdateCalfUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
):UseCase<FireBaseCalf,Flow<Response<Boolean>>>() {

//    suspend operator fun invoke(fireBaseCalf: FireBaseCalf): Flow<Response<Boolean>> {
//
//        return databaseRepository.updateCalf(fireBaseCalf)
//    }

    override suspend fun execute(params: FireBaseCalf): Flow<Response<Boolean>> {
        return databaseRepository.updateCalf(params)
    }
}