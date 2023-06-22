package com.elliottsoftware.calftracker.data.sources

import com.elliottsoftware.calftracker.data.repositories.AuthenticationBlock
import com.elliottsoftware.calftracker.domain.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class FakeFireBaseAuthentication:AuthenticationBlock {

    private val auth: FirebaseAuth = Firebase.auth


    override fun authRegister(email: String, password: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }
}