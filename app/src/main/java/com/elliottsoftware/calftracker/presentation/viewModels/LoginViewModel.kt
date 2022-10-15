package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class LoginUIState(
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false
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
    fun updatePassword(password:String){
        state.value = state.value.copy(password = password)
    }
    private fun validatePassword(password:String):String?{
        if(password.isBlank()){
            return "Password can not be blank"
        }
        if(password.length < 8){
            return "Password must be greater than 8 characters"
        }
        return null
    }
    fun passwordIconChecked(checked:Boolean){
        state.value = state.value.copy(passwordIconChecked= checked)

    }
    fun submitButton(){
        val verifyEmail = validateEmail(state.value.email)
        val verifyPassword = validatePassword(state.value.password)
        state.value = state.value.copy(emailError = verifyEmail, passwordError = verifyPassword)
    }

}