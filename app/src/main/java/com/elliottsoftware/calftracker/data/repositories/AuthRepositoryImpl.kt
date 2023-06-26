package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.presentation.components.login.LoginResult
import com.elliottsoftware.calftracker.util.Actions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


/**
 * The data layer for any requests related to logging in a user */
class AuthRepositoryImpl(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore,
): AuthRepository {


    //TODO:1) change over to callbackFlow
    //TODO:2) remove the SecondaryResponse (can maybe be replaced with a nested when)(move nested when to function)
    override suspend fun authRegister(email: String, password: String,username: String): Flow<Response<Boolean>> = callbackFlow {

            var create:Boolean = false
            trySend(Response.Loading)

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->

                    if(task.isSuccessful){
                        create = true

                    }else{

                        trySend(Response.Failure(Exception()))
                    }

                }.await() //THIS AWAIT COULD BE ANOTHER addOnCompleteListener.
            //WHAT IS HAPPENING ON A addOnFailureListener?

            if(create){
                createUser(email,username).collect{ response ->
                    when(response){
                        is Response.Loading->{
                            //Nothing is done because the initial state is loading
                        }
                        is Response.Success->{
                            trySend(Response.Success(true))
                        }
                        is Response.Failure->{

                            trySend(Response.Failure(Exception("Problem creating the User")))
                        }
                    }

                }
            }

        awaitClose()
    }.catch { cause: Throwable->
        if(cause is FirebaseAuthWeakPasswordException){
            emit(Response.Failure(Exception("Stronger password required")))
        }else{
            emit(Response.Failure(Exception(cause.message?:" unhandled Exception")))
        }
    }

      fun createUser(email: String, username: String)= flow{
          emit(Response.Loading)
        val user = hashMapOf(
            "email" to email,
            "username" to username
        )
            db.collection("users").document(email).set(user).await()
            emit(Response.Success(true))

    }.catch { cause: Throwable->
          emit(Response.Failure(Exception(cause.message?:" unhandled Exception")))
      }

    /**
     * Given a user email and password, try to login the user
     *
     * @return a [Response] or [Response.Success] if successful, [Response.Failure] if otherwise*/
    //TODO: CHANGE THIS OVER FROM AWAIT TO CALLBACKFLOW
    override suspend fun loginUser(email: String, password: String)= callbackFlow {
            //WE MIGHT EVEN NOT NEED THIS RESPONSE.LOADING, WE WILL HAVE TO SEE WHAT IS IMPLEMENTED ON THE VIEW
            trySend(Response.Loading)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        trySend(Response.Success(true))
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.tag("LoginFailure").e(Exception(task.exception))
                        trySend(Response.Failure(Exception()))
                    }
                }

        awaitClose()
    }.catch { cause: Throwable->
            emit(Response.Failure(Exception(cause.message?:" unhandled Exception")))
    }

    override fun isUserSignedIn(): Boolean {
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
                Timber.tag("SENDINGEMAIL").d("resetPassword() called")
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