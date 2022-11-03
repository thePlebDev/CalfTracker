package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth= Firebase.auth
):DatabaseRepository {

    override suspend fun createUser(email: String, username: String)= flow{
        val user = hashMapOf(
            "email" to email,
            "username" to username
        )
        try{
            db.collection("users").document(email).set(user).await()
            emit(SecondaryResponse.SecondActionSuccess)
        }catch (e:Exception){
            Log.d("DatabaseRepository",e.message.toString())
            emit(SecondaryResponse.Failure(e))
        }


    }

    override suspend fun createCalf(calf: FireBaseCalf)=flow {
        try{
            emit(Response.Loading)
           val document = db.collection("users").document(auth.currentUser?.email!!)
                .collection("calves").document()
            calf.id = document.id
            document.set(calf)
            emit(Response.Success(true))
        }catch (e:Exception){
            Log.d("DatabaseRepository",e.message.toString())
            emit(Response.Failure(e))
        }

    }

    override suspend fun getCalves(): Flow<Response<List<FireBaseCalf>>> = flow{
        try {
            emit(Response.Loading)
            val docRef = db.collection("users")
                .document(auth.currentUser?.email!!).collection("calves")
                .get().await().map { document ->
                   document.toObject(FireBaseCalf::class.java)


                }

            emit(Response.Success(docRef))
        }catch (e:Exception){
            emit(Response.Failure(e))
        }
    }
}