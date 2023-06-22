package com.elliottsoftware.calftracker.data.sources

import com.elliottsoftware.calftracker.data.repositories.AuthenticationBlock
import com.elliottsoftware.calftracker.domain.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FireBaseAuthImplementationBlock:AuthenticationBlock {

    private val auth: FirebaseAuth = Firebase.auth


    override fun authRegister(email: String, password: String)= flow {
        emit(Response.Loading)
        var create:Boolean = false
        val result = try{
            Response.Loading
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        true
                    }
                }.await()

            Response.Success(create)

        }catch(e:Exception){
            Response.Failure(e)
        }
        catch (e: FirebaseAuthWeakPasswordException){
            Response.Failure(e)

        }

        emit(result)
    }
}