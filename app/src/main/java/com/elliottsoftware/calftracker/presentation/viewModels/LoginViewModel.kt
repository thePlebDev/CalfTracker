package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.useCases.ValidateEmailUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidatePasswordUseCase

data class LoginUIState(
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false
)

class LoginViewModel(
    val validateEmail:ValidateEmailUseCase = ValidateEmailUseCase(),
    val validatePassword: ValidatePasswordUseCase = ValidatePasswordUseCase()
):ViewModel() {

    var state = mutableStateOf(LoginUIState())
    private set

    fun updateEmail(email: String){
        state.value = state.value.copy(email=email)

    }
    fun updatePassword(password:String){
        state.value = state.value.copy(password = password)
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