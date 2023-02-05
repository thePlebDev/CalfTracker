package com.elliottsoftware.calftracker.domain.useCases

abstract class UseCase<in Params : Any, out Result> {

    abstract suspend fun execute(params: Params): Result
}