package com.elliottsoftware.calftracker.data.repositories

import android.app.Activity
import android.os.Parcel
import com.elliottsoftware.calftracker.domain.models.Response
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

        //BS AUTH RESULT TO GET THINGS GOING
        val authResult = object : AuthResult{
            override fun describeContents(): Int {
                println("authResult  describeContents()")
                return 1
            }

            override fun writeToParcel(p0: Parcel, p1: Int) {
                println("authResult  writeToParcel()")
                return Unit
            }

            override fun getAdditionalUserInfo(): AdditionalUserInfo? {
                println("authResult  getAdditionalUserInfo()")
                return null
            }

            override fun getCredential(): AuthCredential? {
                println("authResult  getCredential()")
                return null
            }

            override fun getUser(): FirebaseUser? {
                println("authResult  getUser()")
                // returns null representing the user is not logged in
                return null
            }
        }

        failureTask = object : Task<AuthResult>() {


            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                println("addOnFailureListener  1")
                return failureTask
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                println("addOnFailureListener  Activity")
                return failureTask
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                println("addOnFailureListener Executor")
                return failureTask
            }

            //BY RETURNING AN EXCEPTION WE MIGHT BE ABLE TO TEST THE EXCEPTION CODE
            override fun getException(): Exception? {
                println("getException")
                return null
            }

            override fun getResult(): AuthResult {
                println("getResult")
                return authResult
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult {
                println("getResult pO")
                return authResult
            }

            override fun isCanceled(): Boolean {
                println("isCanceled")
                return false
            }

            override fun isComplete(): Boolean {
                println("isComplete")
                return true
            }

            //I THINK THIS IS HOW WE DETERMINE IF task.isSuccessful
            override fun isSuccessful(): Boolean {
                println("isSuccessful Executor")
                return false
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return failureTask
                println("addOnSuccessListener Executor")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                println("addOnSuccessListener ACTIVITY")
                return failureTask
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                println("addOnSuccessListener")
                return failureTask
            }

            override fun addOnCompleteListener(p0: OnCompleteListener<AuthResult>): Task<AuthResult> {

                return failureTask
            }
        }




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

         val mAuth = mockk<FirebaseAuth>()
         val outerClass = OutterClass(mAuth)
        val testEmail = "BOB@bobmail.com"
        val testPassword = "fdsafdsafdsafdsafdsa"

        every { mAuth.createUserWithEmailAndPassword(testEmail,testPassword) } throws Exception("MEATBALLS")





        val result =outerClass.authRegister(
            email = testEmail,password = testPassword
        ).drop(1).first()

        assertEquals(Response.Loading,result)
    }

 }





