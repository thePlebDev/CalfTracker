package com.elliottsoftware.calftracker.data.sources.implementations

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

            }.await() //THIS AWAIT COULD BE ANOTHER addOnCompleteListener.
        //WHAT IS HAPPENING ON A addOnFailureListener?

        awaitClose()
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

//    override fun currentUser(): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun signUserOut(): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun resetPassword(email: String): Flow<Response<Boolean>> {
//        TODO("Not yet implemented")
//    }

}