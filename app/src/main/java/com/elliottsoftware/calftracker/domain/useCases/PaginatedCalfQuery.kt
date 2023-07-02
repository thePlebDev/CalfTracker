package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaginatedCalfQuery @Inject constructor(
    val databaseRepository:DatabaseRepository
):UseCase<Long, Flow<Response<List<FireBaseCalf>>>>() {
    private val auth: FirebaseAuth = Firebase.auth
    override suspend fun execute(queryLimit:Long): Flow<Response<List<FireBaseCalf>>> {

      return databaseRepository.getCalves(queryLimit,auth.currentUser?.email!!)

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