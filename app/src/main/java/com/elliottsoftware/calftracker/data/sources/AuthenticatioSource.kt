package com.elliottsoftware.calftracker.data.sources

import com.elliottsoftware.calftracker.domain.models.Response
import kotlinx.coroutines.flow.Flow

/**
 * A interface to represent a clean interaction between the code and a authentication source

 */
interface AuthenticationSource {


    /**
     * Call to create a User with a email and password inside of a authentication system
     *
     * This class has no useful logic; it's just a documentation example.
     *
     * @param email provided by the user and used inside of a authentication system
     * @param password provided by the user and used inside of a authentication system
     *
     * @return returns a flow of [Response] determining the status of the response
     */
    fun createUserWithEmailAndPassword(email:String,password:String): Flow<Response<Boolean>>

    /**
     * Call to log a user in with their email and password
     *
     *
     * @param email provided by the user and used to login the user
     * @param password provided by the user and used to login the user
     *
     * @return returns a flow of [Response] determining the status of the response
     */
    fun loginWithEmailAndPassword(email:String, password:String): Flow<Response<Boolean>>


    /**
     * Call to determine if the user is logged in or not
     *
     *
     * @return returns a boolean to determine if the user is logged in or not
     */
    fun isUserSignedIn():Boolean

    /**
     * Call to logout the user from their current session
     *
     * @return returns a boolean to determine if the logout was successful
     */
    fun signUserOut(): Boolean

    /**
     * Call to send a reset password email to the user
     *
     *
     * @param email provided by the user to confirm their identity inside the authentication system
     * the reset password should be sent to this email
     *
     * @return returns a flow of [Response] determining the status of the response
     */
    fun resetPassword(email:String): Flow<Response<Boolean>>

    fun currentUserEmail():String





}