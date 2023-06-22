package com.elliottsoftware.calftracker.data.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface AuthenticationBlock {

    fun authRegister(email:String,password:String): Flow<Response<Boolean>>
}