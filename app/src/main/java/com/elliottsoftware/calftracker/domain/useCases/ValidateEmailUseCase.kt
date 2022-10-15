package com.elliottsoftware.calftracker.domain.useCases

class ValidateEmailUseCase {

    operator fun invoke(email:String):String?{
        if(email.isBlank()){
            return "Email can not be blank"
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Enter a valid email"
        }
        return null
    }

}

