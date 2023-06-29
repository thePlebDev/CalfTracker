package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.State
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
    val signInWithFirebaseResponse:Response<Boolean> =Response.Success(false),
    val buttonEnabled:Boolean = true,
    val registerError:Boolean = false,
    val errorMessage:String? = null

)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val validateEmail: ValidateEmailUseCase,
    val validateUsername: ValidateUsernameUseCase,
    val validatePassword: ValidatePasswordUseCase,
    val registerUserUseCase: RegisterUserUseCase,
    val createUserUseCase: CreateUserUseCase
):ViewModel() {

    private var _uiState = mutableStateOf(RegisterUIState())
    val state:State<RegisterUIState> = _uiState

    private val stateFlow = MutableStateFlow(false)



    init {
        viewModelScope.launch {
            stateFlow.collect{
                if(it){

                    createUserDatabase(_uiState.value.email,_uiState.value.username)
                }
            }
        }
    }


    fun updateUsername(username: String){
        _uiState.value = _uiState.value.copy(username = username)
    }
    fun updatePassword(password: String){
        _uiState.value = _uiState.value.copy(password = password)
    }
    fun updateEmail(email: String){
        _uiState.value = _uiState.value.copy(email=email)

    }
    fun passwordIconChecked(checked:Boolean){
        _uiState.value = _uiState.value.copy(passwordIconChecked= checked)

    }

    //goes first
    private fun signUpUserAuthRepository(email:String, password:String,username:String) =viewModelScope.launch{
         registerUserUseCase.execute(RegisterUserParams(email, password,username)).collect{response ->
            // signInWithFirebaseResponse = response

             _uiState.value = _uiState.value.copy(signInWithFirebaseResponse = response)
             when(response){
                 is Response.Loading -> {
                     _uiState.value = _uiState.value.copy(
                         buttonEnabled = false,
                         registerError = false
                     )
                     stateFlow.emit(false)
                 }
                 is Response.Success -> {
                    // _uiState.value = _uiState.value.copy(buttonEnabled = true)
                     stateFlow.emit(true)
                 }
                 is Response.Failure -> {
                     _uiState.value = _uiState.value.copy(
                         buttonEnabled = true,
                         registerError = true,
                         errorMessage = response.e.message

                     )
                 }

             }
         }

    }

        //goes second
    //Creates the user inside the realtime database.
        private fun createUserDatabase(email:String, username:String) = viewModelScope.launch{
        createUserUseCase.execute(CreateUserParams(email,username)).collect{response ->

            _uiState.value = _uiState.value.copy(signInWithFirebaseResponse = response)
        }

    }

    fun submitButton(){
        val verifyEmail = validateEmail(_uiState.value.email)
        val verifyUsername = validateUsername(_uiState.value.username)
        val verifyPassword = validatePassword(_uiState.value.password)
       // val verifyPassword = validatePassword(state.value.password)
        _uiState.value = _uiState.value.copy(emailError = verifyEmail, usernameError = verifyUsername,
            passwordError = verifyPassword)
        if(verifyUsername == null && verifyEmail == null && verifyPassword == null){
            signUpUserAuthRepository(_uiState.value.email,_uiState.value.password, _uiState.value.username)
        }
    }

}