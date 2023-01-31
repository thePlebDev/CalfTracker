package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.util.Actions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl(

    private val auth: FirebaseAuth = Firebase.auth
): AuthRepository {


    //TODO:1) change over to callbackFlow
    //TODO:2) remove the SecondaryResponse (can maybe be replaced with a nested when)(move nested when to function)
    override suspend fun authRegister(email: String, password: String) = callbackFlow {
        try {
            trySend(Response.Loading)

            auth.createUserWithEmailAndPassword(email,password).await() //await() integrates with the Google task API //DONE
            trySend(Response.Success(Actions.FIRST))
        }catch (e:Exception){ //gets triggered on email already in use
            Log.d("AuthRepositoryImpl",e.message.toString())
            trySend(Response.Failure(e))
        }
        awaitClose()
    }

    //TODO: CHANGE THIS OVER FROM AWAIT TO CALLBACKFLOW
    override suspend fun loginUser(email: String, password: String)= flow {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).await() //can throw FirebaseAuthInvalidUserException //DONE
            emit(Response.Success(true))
            Timber.tag("LoginSuccess").d(email)
        }catch (e:Exception){

            Timber.tag("LoginFailure").e(e)
            emit(Response.Failure(e))
        }
    }

    override  fun isUserSignedIn(): Boolean {
        try{
            val auth = auth.currentUser // TODO THIS ONE RETURNS A VALUE, SO COME BACK TO IT
            return auth != null
        }catch (e:Exception){
            Timber.e(e)
            return false
        }

    }

    override fun signUserOut(): Boolean {
        try{
            auth.signOut() //DONE

            return true

        }catch (e:Exception){
            Timber.e(e)
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

//    //TESITNG
//    //TODO: So apparently flow does not need a suspend keyword
//      fun MEATBALLTEXT(email: String, password: String)= flow {
//        try {
//            emit(Response.Loading)
//            auth.signInWithEmailAndPassword(email, password).await() //can throw FirebaseAuthInvalidUserException //DONE
//            emit(Response.Success(true))
//        }catch (e:Exception){
//            Timber.e(e)
//            emit(Response.Failure(e))
//        }
//    }

}