package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.useCases.ValidateEmailUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidatePasswordUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidateUsernameUseCase

data class RegisterUIState(
    val username:String = "",
    val usernameError:String? = null,
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false
)

class RegisterViewModel(
    val validateEmail: ValidateEmailUseCase = ValidateEmailUseCase(),
    val validateUsername: ValidateUsernameUseCase = ValidateUsernameUseCase(),
    val validatePassword: ValidatePasswordUseCase = ValidatePasswordUseCase()
):ViewModel() {

    var state = mutableStateOf(RegisterUIState())



    fun updateUsername(username: String){
        state.value = state.value.copy(username = username)
    }
    fun updatePassword(password: String){
        state.value = state.value.copy(password = password)
    }
    fun updateEmail(email: String){
        state.value = state.value.copy(email=email)

    }
    fun passwordIconChecked(checked:Boolean){
        state.value = state.value.copy(passwordIconChecked= checked)

    }

    fun submitButton(){
        val verifyEmail = validateEmail(state.value.email)
        val verifyUsername = validateUsername(state.value.username)
        val verifyPassword = validatePassword(state.value.password)
       // val verifyPassword = validatePassword(state.value.password)
        state.value = state.value.copy(emailError = verifyEmail, usernameError = verifyUsername,
            passwordError = verifyPassword)
    }

}