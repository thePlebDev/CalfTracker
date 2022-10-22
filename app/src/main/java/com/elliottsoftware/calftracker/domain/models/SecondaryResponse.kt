package com.elliottsoftware.calftracker.domain.models

sealed class SecondaryResponse<out T> {
    object Loading:SecondaryResponse<Nothing>()
    object SecondActionSuccess:SecondaryResponse<Nothing>()

    data class Success<out T>(
        val data:T
    ):SecondaryResponse<T>()

    data class Failure(
        val e:Exception
    ):SecondaryResponse<Nothing>()
}