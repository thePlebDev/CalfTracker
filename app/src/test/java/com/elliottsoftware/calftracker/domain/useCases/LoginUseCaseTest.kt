package com.elliottsoftware.calftracker.domain.useCases


import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class LoginUseCaseTest{

//    private lateinit var authRepository: FakeAuthRepository
//
//    @Before
//    fun setup(){
//        authRepository =FakeAuthRepository()
//    }
//
//    @Test
//    fun testSuccessfulLogin()=runTest{
//        val loginResponse = Response.Success(true)
//
//        val sut = LoginUseCase(authRepository)
//        val items = sut.execute(LoginParams("emai","1123")).first()
//
//        //expected:<ITEM_1> but was:<Success(data=true)>
//        assertEquals("ITEM_1", items)
//
//    }
}