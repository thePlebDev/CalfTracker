package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.CheckUserLoggedInUseCase
import com.elliottsoftware.calftracker.domain.useCases.LoginUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidateEmailUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUIState(
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false,
    val isUserLoggedIn:Boolean = false,
    val loginStatus: Response<Boolean> = Response.Success(false)
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail:ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val checkUserLoggedIn: CheckUserLoggedInUseCase,
    private val loginUseCase: LoginUseCase
):ViewModel() {
     var state:MutableState<LoginUIState> = mutableStateOf(LoginUIState())
        private set

    init {

        checkLogInStatus()
    }


    private fun loginUser(email: String,password:String)= viewModelScope.launch{
        loginUseCase(email,password).collect{ response ->
            state.value = state.value.copy(loginStatus = response)
        }
    }


    fun updateEmail(email: String){
        state.value = state.value.copy(email=email)

    }
    fun updatePassword(password:String){
        state.value = state.value.copy(password = password)
    }
    private fun checkLogInStatus(){
        val auth = checkUserLoggedIn.invoke()
        state.value = state.value.copy(isUserLoggedIn = auth)

    }


    fun passwordIconChecked(checked:Boolean){
        state.value = state.value.copy(passwordIconChecked= checked)

    }


    fun submitButton(){
        val verifyEmail = validateEmail(state.value.email)
        val verifyPassword = validatePassword(state.value.password)
        state.value = state.value.copy(emailError = verifyEmail, passwordError = verifyPassword)

        if(state.value.emailError == null && state.value.passwordError == null){
            loginUser(state.value.email,state.value.password)
        }
    }

}