package com.elliottsoftware.calftracker.domain.models

/**
 * Represents a network response
 *
 * This class represents the 3 possible states of network responses in this application.
 * - [Response.Loading]
 * - [Response.Success]
 * - [Response.Failure]
 *
 * @param T the value returned from the network request
 */
sealed class Response<out T> {

    /**
     *
     * This class represents the loading state of a network request
     *
     */
    object Loading: Response<Nothing>()



    /**
     *This class represents the successful return state of a network request
     *
     * @param data the value returned from the network request
     */
    data class Success<out T>(
        val data:T
    ):Response<T>()

    /**
     *This class represents the failed state of a network request
     *
     * @param e the exception causing the failed request
     */
    data class Failure(
        val e:Exception
    ):Response<Nothing>()

}