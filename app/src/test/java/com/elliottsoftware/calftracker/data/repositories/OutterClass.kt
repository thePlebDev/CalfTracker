package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.sources.FireBaseAuthImplementationBlock
import com.elliottsoftware.calftracker.domain.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OutterClass(
    private val auth: AuthenticationBlock = FireBaseAuthImplementationBlock(),
) {


    suspend fun authRegister(email:String,password:String): Flow<Response<Boolean>> = flow{
        auth.authRegister(email, password).collect{ response ->
            when(response){
                is Response.Loading -> {
                    emit(response)
                }
                is Response.Success -> {
                    emit(response)
                }
                is Response.Failure -> {
                    emit(response)
                }
            }
        }
    }
}