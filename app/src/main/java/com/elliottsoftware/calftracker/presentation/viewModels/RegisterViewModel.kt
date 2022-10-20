package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.RegisterUserUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidateEmailUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidatePasswordUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidateUsernameUseCase
import kotlinx.coroutines.launch

data class RegisterUIState(
    val username:String = "",
    val usernameError:String? = null,
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false,
    val signInWithFirebaseResponse:Response<Boolean> =Response.Success(false)
)

class RegisterViewModel(
    val validateEmail: ValidateEmailUseCase = ValidateEmailUseCase(),
    val validateUsername: ValidateUsernameUseCase = ValidateUsernameUseCase(),
    val validatePassword: ValidatePasswordUseCase = ValidatePasswordUseCase(),
    val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCase()
):ViewModel() {

    var state = mutableStateOf(RegisterUIState())
    //var signInWithFirebaseResponse by mutableStateOf<Response<Boolean>>(Response.Success(false))




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

    private fun signUpUser(email:String, password:String) =viewModelScope.launch{
         registerUserUseCase(email, password).collect{response ->
            // signInWithFirebaseResponse = response

             state.value = state.value.copy(signInWithFirebaseResponse = response)
         }

    }

    fun submitButton(){
        val verifyEmail = validateEmail(state.value.email)
        val verifyUsername = validateUsername(state.value.username)
        val verifyPassword = validatePassword(state.value.password)
       // val verifyPassword = validatePassword(state.value.password)
        state.value = state.value.copy(emailError = verifyEmail, usernameError = verifyUsername,
            passwordError = verifyPassword)
        if(verifyUsername == null && verifyEmail == null && verifyPassword == null){
            signUpUser(state.value.email,state.value.password)
        }
    }

}