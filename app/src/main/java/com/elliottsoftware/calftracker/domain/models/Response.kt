package com.elliottsoftware.calftracker.domain.models

//Response is the super class and all nested inside are its sub-classes
sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data:T
    ):Response<T>()

    data class Failure(
        val e:Exception
    ):Response<Nothing>()

}