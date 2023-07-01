package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.presentation.components.login.LoginResult
import com.elliottsoftware.calftracker.util.Actions
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
   // fun isUserAuthenticatedInFirebase(): Boolean

   // suspend fun firebaseSignInEmailNPassword(email:String, password:String): Flow<Response2<Boolean>>

     fun authRegister(email:String,password:String):Flow<Response<Boolean>>

     fun createUser(email: String, username: String):Flow<Response<Boolean>>

     fun loginUser(email:String,password:String):Flow<Response<Boolean>>

     fun isUserSignedIn():Boolean

     fun signUserOut():Boolean

     fun resetPassword(email:String):Flow<Response<Boolean>>

    fun testingThings(email:String,password: String): Flow<Response<Boolean>>


}