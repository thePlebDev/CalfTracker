package com.elliottsoftware.calftracker.data.sources

import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface AuthenticationSource {
    // I need a createUserWithEmailAndPassword() DONE
    // a signInWithEmailAndPassword() DONE
    // a currentUser  DONE
    // a signOut() DONE
    // a sendPasswordResetEmail()

    fun createUserWithEmailAndPassword(email:String,password:String): Flow<Response<Boolean>>

    fun loginWithEmailAndPassword(email:String, password:String): Flow<Response<Boolean>>


    fun currentUser():Boolean

    fun signUserOut(): Boolean

    fun resetPassword(email:String): Flow<Response<Boolean>>





}