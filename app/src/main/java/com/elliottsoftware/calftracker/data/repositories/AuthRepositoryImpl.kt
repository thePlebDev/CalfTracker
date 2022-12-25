package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.source.FirebaseAuthenticationSource
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    //TODO: change this to a AuthenticationSource interface class
    // they we have that interface implementation call to the Firebase.auth
    // create a source package and add this new class to it
    // we will use the delegation pattern
    private val auth: FirebaseAuthenticationSource= FirebaseAuthenticationSource()
): AuthRepository {


    override suspend fun authRegister(email: String, password: String)= flow {
        try {
            emit(SecondaryResponse.Loading)
            auth.createUserWithEmailAndPassword(email,password).await() //await() integrates with the Google task API //DONE
            emit(SecondaryResponse.Success(true))
        }catch (e:Exception){
            Log.d("AuthRepositoryImpl",e.message.toString())
            emit(SecondaryResponse.Failure(e))
        }
    }

    override suspend fun loginUser(email: String, password: String)= flow {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).await() //can throw FirebaseAuthInvalidUserException //DONE
            emit(Response.Success(true))
        }catch (e:Exception){
            Log.d("AuthRepositoryImpl",e.message.toString())
            emit(Response.Failure(e))
        }
    }

    override  fun isUserSignedIn(): Boolean {
        val auth = auth.currentUser // TODO THIS ONE RETURNS A VALUE, SO COME BACK TO IT
        return auth != null
    }

    override fun signUserOut(): Boolean {
        try{
            auth.signOut() //DONE
            return true

        }catch (e:Exception){
            Log.d("AuthRepositoryImpl:isUserSignedIn()",e.message.toString())
            return false
        }

    }

    override suspend fun resetPassword(email:String)= callbackFlow {
        trySend(Response.Loading)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Response.Success(true))
            }
        }
            .addOnCanceledListener {
                Log.d("CANCELED","CANCELED")
                trySend(Response.Failure(Exception("CANCELED")))
            }
            .addOnFailureListener{ exception ->
                Log.d("FAIL",exception.message.toString())
                trySend(Response.Failure(exception))

            }
        awaitClose()
    }

}