package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
   // fun isUserAuthenticatedInFirebase(): Boolean

   // suspend fun firebaseSignInEmailNPassword(email:String, password:String): Flow<Response2<Boolean>>

    suspend fun authRegister(email:String,password:String):Flow<SecondaryResponse<Boolean>>

    suspend fun loginUser(email:String,password:String):Flow<Response<Boolean>>

     fun isUserSignedIn():Boolean

     fun signUserOut():Boolean

     suspend fun resetPassword(email:String):Flow<Response<Boolean>>

}