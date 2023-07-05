package com.elliottsoftware.calftracker.data.sources.implementations

import android.util.Log
import com.elliottsoftware.calftracker.data.sources.AuthenticationSource
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FireBaseAuthentication : AuthenticationSource {

    private val auth: FirebaseAuth = Firebase.auth


   // fun createUserWithEmailAndPassword(email:String,password:String): Flow<Response<Boolean>>
    override fun createUserWithEmailAndPassword(email: String, password: String): Flow<Response<Boolean>> = callbackFlow {


        trySend(Response.Loading)

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->

                if(task.isSuccessful){
                    // createUser(email,username)
                    trySend(Response.Success(true))

                }else{

                    trySend(Response.Failure(Exception()))
                }

            }.await()

        awaitClose()
    }

    override fun loginWithEmailAndPassword(email: String, password: String)= callbackFlow {
        trySend(Response.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Response.Success(true))
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag("LoginFailure").e(Exception(task.exception))
                    trySend(Response.Failure(Exception()))
                }
            }

        awaitClose()
    }


    override fun isUserSignedIn(): Boolean {

            val auth = auth.currentUser // TODO THIS ONE RETURNS A VALUE, SO COME BACK TO IT
            return auth != null
    }

    override fun signUserOut(): Boolean {

           val signedOut = auth.signOut() //DONE

            return true


    }
//
    override fun resetPassword(email: String): Flow<Response<Boolean>> = callbackFlow{
        trySend(Response.Loading)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.tag("SENDINGEMAIL").d("resetPassword() called")
                    trySend(Response.Success(true))
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("FAIL",exception.message.toString())
                trySend(Response.Failure(exception))

            }
        awaitClose()
    }

    override fun currentUserEmail():String{
        return auth.currentUser?.email!!

    }

}