package com.elliottsoftware.calftracker.domain.useCases

class ValidateUsernameUseCase {

    operator fun invoke(username:String):String?{
        if(username.isBlank()){
            return "Username can not be blank"
        }

        return null
    }
}