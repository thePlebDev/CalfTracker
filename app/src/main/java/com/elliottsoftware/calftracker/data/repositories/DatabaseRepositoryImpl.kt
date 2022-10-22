package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val db: FirebaseFirestore = Firebase.firestore
):DatabaseRepository {

    override suspend fun createUser(email: String, username: String)= flow{
        val user = hashMapOf(
            "email" to email,
            "username" to username
        )
        try{

            db.collection("users").add(user).await()
            emit(SecondaryResponse.SecondActionSuccess)
        }catch (e:Exception){
            Log.d("DatabaseRepository",e.message.toString())
            emit(SecondaryResponse.Failure(e))
        }


    }
}