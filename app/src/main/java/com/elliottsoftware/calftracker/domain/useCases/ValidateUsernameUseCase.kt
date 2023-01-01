package com.elliottsoftware.calftracker.domain.useCases

import javax.inject.Inject

class ValidateUsernameUseCase  @Inject constructor(){

    operator fun invoke(username:String):String?{
        if(username.isBlank()){
            return "Username can not be blank"
        }

        return null
    }
}