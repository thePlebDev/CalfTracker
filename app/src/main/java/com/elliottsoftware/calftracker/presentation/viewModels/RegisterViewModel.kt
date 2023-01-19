package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.SecondaryResponse
import com.elliottsoftware.calftracker.domain.useCases.*
import com.elliottsoftware.calftracker.util.Actions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUIState(
    val username:String = "",
    val usernameError:String? = null,
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false,
    //todo: CHANGE BOOLEAN TO A MORE EXPLICIT VIEWMODEL
    val signInWithFirebaseResponse:Response<Actions> =Response.Success(Actions.RESTING),

)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val validateEmail: ValidateEmailUseCase,
    val validateUsername: ValidateUsernameUseCase,
    val validatePassword: ValidatePasswordUseCase,
    val registerUserUseCase: RegisterUserUseCase,
    val createUserUseCase: CreateUserUseCase
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

    private fun signUpUserAuthRepository(email:String, password:String) =viewModelScope.launch{
         registerUserUseCase(email, password).collect{response ->
            // signInWithFirebaseResponse = response

             state.value = state.value.copy(signInWithFirebaseResponse = response)
         }

    }

    //Creates the user inside the realtime database
    fun createUserDatabase(email:String, password:String) = viewModelScope.launch{
        createUserUseCase(email,password).collect{response ->
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
            signUpUserAuthRepository(state.value.email,state.value.password)
        }
    }

}