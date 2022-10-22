package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
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
            emit(SecondaryResponse.Loading)
            auth.createUserWithEmailAndPassword(email,password).await() //await() integrates with the Google task API
            emit(SecondaryResponse.Success(true))
        }catch (e:Exception){
            Log.d("AuthRepositoryImpl",e.message.toString())
            emit(SecondaryResponse.Failure(e))
        }
    }

    override suspend fun loginUser(email: String, password: String)= flow {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).await() //can throw FirebaseAuthInvalidUserException
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

    override fun signUserOut(): Boolean {
        try{
            auth.signOut()
            return true

        }catch (e:Exception){
            Log.d("AuthRepositoryImpl:isUserSignedIn()",e.message.toString())
            return false
        }

    }

}