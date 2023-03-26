package com.elliottsoftware.calftracker.data.source

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.util.Actions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A fake implementation of a [AuthRepository] that wraps all of our work*/
class FakeAuthRepository():AuthRepository {


    val mock:AuthRepository = mockk()
    var success:Boolean = true


//    fun mockLoginWithCredentials(
//        email:String,password:String,
//        result:Flow<Response<Boolean>>
//    ){
//        coEvery {
//            mock.loginUser(email,password)
//        } returns result
//    }

    override suspend fun authRegister(email: String, password: String): Flow<Response<Actions>> = flow  {
        emit(Response.Success(Actions.RESTING))
    }

    override suspend fun loginUser(email: String, password: String): Flow<Response<Boolean>> = flow {

        if(success){
            emit(Response.Success(true))

        }else{
            emit(Response.Failure(Exception("failure")))
        }
    }

    override fun isUserSignedIn(): Boolean {
        return true;
    }

    override fun signUserOut(): Boolean {
        return true;
    }

    override suspend fun resetPassword(email: String): Flow<Response<Boolean>> = flow {
        emit(Response.Success(true))
    }


}