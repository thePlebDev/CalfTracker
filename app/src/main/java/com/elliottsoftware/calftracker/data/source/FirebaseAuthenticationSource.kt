package com.elliottsoftware.calftracker.data.source

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//TODO: Ok, so we do everything but we just use Mock.mock() for the auth implementation
// this abstraction still helps with testing
//but remove the interface, the return values are too specific
class FirebaseAuthenticationSource(
    private val auth: FirebaseAuth = Firebase.auth
) {

    val currentUser get() = auth.currentUser

    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email,password)
    }

    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }


    fun signOut() {
        return auth.signOut()
    }

    fun sendPasswordResetEmail(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }
}