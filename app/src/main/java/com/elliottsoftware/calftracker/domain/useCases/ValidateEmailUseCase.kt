package com.elliottsoftware.calftracker.domain.useCases

import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {

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

