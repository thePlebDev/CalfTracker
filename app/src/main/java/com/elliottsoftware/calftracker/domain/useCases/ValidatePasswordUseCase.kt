package com.elliottsoftware.calftracker.domain.useCases

class ValidatePasswordUseCase {

    operator fun invoke(password:String):String?{
        if(password.isBlank()){
            return "Password can not be blank"
        }
        if(password.length < 8){
            return "Password must be greater than 8 characters"
        }
        return null
    }
}