package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.sources.AuthenticationSource
import com.elliottsoftware.calftracker.data.sources.DatabaseSource
import com.elliottsoftware.calftracker.data.sources.implementations.FireBaseAuthentication
import com.elliottsoftware.calftracker.data.sources.implementations.FireBaseFireStore
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.presentation.components.login.LoginResult
import com.elliottsoftware.calftracker.util.Actions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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
    private val authenticationSource:AuthenticationSource  = FireBaseAuthentication(),
    private val databaseSource:DatabaseSource = FireBaseFireStore()

): AuthRepository {

   override fun testingThings(email:String,password: String): Flow<Response<Boolean>> {

        val items = authenticationSource.createUserWithEmailAndPassword(email, password)
            .catch { cause: Throwable->
                if(cause is FirebaseAuthWeakPasswordException){
                    emit(Response.Failure(Exception("Stronger password required")))
                }
                if(cause is FirebaseAuthInvalidCredentialsException){
                    emit(Response.Failure(Exception("Invalid credentials")))
                }
                if(cause is FirebaseAuthUserCollisionException){
                    emit(Response.Failure(Exception("Email already exists")))
                }
                else{
                    emit(Response.Failure(Exception("Error! Please try again")))
                }
            }
        return items
    }

    override fun authRegister(email: String, password: String): Flow<Response<Boolean>> {
        val items = authenticationSource.createUserWithEmailAndPassword(email, password)
            .catch { cause: Throwable->
                if(cause is FirebaseAuthWeakPasswordException){
                    emit(Response.Failure(Exception("Stronger password required")))
                }
                if(cause is FirebaseAuthInvalidCredentialsException){
                    emit(Response.Failure(Exception("Invalid credentials")))
                }
                if(cause is FirebaseAuthUserCollisionException){
                    emit(Response.Failure(Exception("Email already exists")))
                }
                else{
                    emit(Response.Failure(Exception("Error! Please try again")))
                }
            }

        return items


    }

    //TODO: THIS NEEDS TO BE CALLED AFTER A SUCCESSFUL authRegister()
      override fun createUser(email: String, username: String):Flow<Response<Boolean>>{
       val items = databaseSource.createUser(email,username)
           .catch { cause: Throwable->
           emit(Response.Failure(Exception(" Error! Please try again")))
       }
        return items
    }

    /**
     * Given a user email and password, try to login the user
     *
     * @return a [Response] or [Response.Success] if successful, [Response.Failure] if otherwise*/
    override fun loginUser(email: String, password: String):Flow<Response<Boolean>>{

            val items = authenticationSource.loginWithEmailAndPassword(email, password)
                .catch { cause: Throwable->
                    emit(Response.Failure(Exception("Error! Please try again")))
                }

        return items
    }

    override fun isUserSignedIn(): Boolean {
        try{
            return authenticationSource.isUserSignedIn()
        }catch (e:Exception){
            Timber.e(e)
            return false
        }

    }

    override fun signUserOut(): Boolean {
        try{

            return authenticationSource.signUserOut()
        }catch (e:Exception){
            Timber.e(e)
            return false
        }

    }

    override fun resetPassword(email:String):Flow<Response<Boolean>> {
        val items = authenticationSource.resetPassword(email)
            .catch { cause: Throwable->
                emit(Response.Failure(Exception("Error! Please try again")))
            }
        return items
    }


}

