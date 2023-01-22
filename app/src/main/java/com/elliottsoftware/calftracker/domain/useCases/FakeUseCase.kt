package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.domain.models.NetworkResponse
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class FakeUseCase {
//}
class FakeUseCase @Inject constructor(
    val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(): Flow<NetworkResponse<List<FireBaseCalf>>> {
        return databaseRepository.getCalvesTest()
    }

}