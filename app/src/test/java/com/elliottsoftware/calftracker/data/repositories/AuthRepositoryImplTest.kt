package com.elliottsoftware.calftracker.data.repositories

import android.app.Activity
import android.os.Parcel
import com.elliottsoftware.calftracker.data.sources.AuthenticationSource
import com.elliottsoftware.calftracker.data.sources.implementations.FireBaseAuthentication
import com.elliottsoftware.calftracker.domain.models.Response
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.MockitoAnnotations
import timber.log.Timber
import java.util.concurrent.Executor
import kotlin.Exception


class AuthRepositoryImplTest{

    private lateinit var authRepositoryImplUnderTest: AuthRepositoryImpl
    private lateinit var failureTask: Task<AuthResult>


    private val mAuth = mockk<FirebaseAuth>()
    private val db = mockk<FirebaseFirestore>()



    @Before
    fun setUp(){
        authRepositoryImplUnderTest = AuthRepositoryImpl(mAuth,db) //INITIALIZING THE UNDERTEST REPOSITORY






        //BOTTOM OF THE SETUP

    }

     // Each test is annotated with @Test (this is a Junit annotation)
     @OptIn(ExperimentalCoroutinesApi::class)
     @Test
     fun addition_isCorrect() = runTest(UnconfinedTestDispatcher()){
         // Here you are checking that 4 is the same as 2+2
         val testEmail = "BOB@Bobmail.com"
         val testPassword = "3334dsfadf"


         every { mAuth.createUserWithEmailAndPassword(testEmail,testPassword) } returns failureTask


         // Take the second item
        // outputFlow.drop(1).first()
         launch{
             val result = authRepositoryImplUnderTest
                 .authRegister(
                     email=testEmail,
                     password=testPassword,
                     username="meatballersdd"
                 ).drop(1).first()


             assertEquals(Response.Loading, result)
         }
     }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun tryingToMimicThings() = runTest{
         val innerClass = mockk<InnerClass>()

//         val mAuth = mockk<FirebaseAuth>()
//         val outerClass = OutterClass(mAuth)
//        val testEmail = "BOB@bobmail.com"
//        val testPassword = "fdsafdsafdsafdsafdsa"
//
//        every { mAuth.createUserWithEmailAndPassword(testEmail,testPassword) } throws Exception("MEATBALLS")
//
//
//
//
//
//        val result =outerClass.authRegister(
//            email = testEmail,password = testPassword
//        ).drop(1).first()
//
//        assertEquals(Response.Loading,result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_fireBase_auth_wrapper() = runTest{
        val testEmail = "ANOTHER"
        val testPassword  = "MEATBALL"
        val authenticationSource = FakeAuthenticationSource()

        val fakeRepoLayer:FakeRepoLayer = FakeRepoLayer(authenticationSource)
        //every { authenticationSource.createUserWithEmailAndPassword(testEmail,testPassword) } throws Exception("another")


        val result = fakeRepoLayer.testingThings(testEmail,testPassword).first()
        assertEquals(Response.Loading,result)

    }

 }

class FakeRepoLayer(
    private val thingers: AuthenticationSource
){
    fun testingThings(email: String,password: String): Flow<Response<Boolean>> {

        val items = thingers.createUserWithEmailAndPassword(email,password)
            .catch { cause: Throwable->
                if(cause is FirebaseAuthWeakPasswordException){
                    emit(Response.Failure(Exception("Stronger password required")))
                }
                if(cause is FirebaseAuthInvalidCredentialsException){
                    emit(Response.Failure(Exception("Invalid credentials")))
                }
                if(cause is FirebaseAuthUserCollisionException){
                    emit(Response.Failure(Exception("Email already exists")))
                }
                else{
                    emit(Response.Failure(Exception("Error! Please try again")))
                }
            }
        return items
    }


}
class FakeAuthenticationSource():AuthenticationSource{

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow{
        emit(Response.Loading)
        throw Exception("ANOTHER")


    }

}





