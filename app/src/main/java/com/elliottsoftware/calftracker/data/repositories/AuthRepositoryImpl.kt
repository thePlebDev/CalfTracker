package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth= Firebase.auth
): AuthRepository {


    override suspend fun authRegister(email: String, password: String)= flow {
        try {
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(email,password).await()
            emit(Response.Success(true))
        }catch (e:Exception){
            Log.d("AuthRepositoryImpl",e.message.toString())
            emit(Response.Failure(e))
        }
    }

    override  fun isUserSignedIn(): Boolean {
        val auth = auth.currentUser
        return auth != null
    }

}