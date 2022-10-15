package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class LoginUIState(
    val email:String = "",
    val emailError:String? = null
)

class LoginViewModel:ViewModel() {

    var state = mutableStateOf(LoginUIState())
    private set

    fun updateEmail(email: String){
        state.value = state.value.copy(email=email)

    }
    private fun validateEmail(email:String):String?{
        if(email.isBlank()){
            return "Email can not be blank"
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Enter a valid email"
        }
        return null
    }
    fun submitButton(){
        val verifyEmail = validateEmail(state.value.email)
        state.value = state.value.copy(emailError = verifyEmail)
    }

}