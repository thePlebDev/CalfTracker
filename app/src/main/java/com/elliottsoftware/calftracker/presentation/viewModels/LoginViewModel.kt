package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.elliottsoftware.calftracker.domain.useCases.CheckUserLoggedInUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidateEmailUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class LoginUIState(
    val email:String = "",
    val emailError:String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val passwordIconChecked:Boolean = false,
    val showProgressBar:Boolean = false,
    val isUserLoggedIn:Boolean = false
)

class LoginViewModel(
    private val validateEmail:ValidateEmailUseCase = ValidateEmailUseCase(),
    private val validatePassword: ValidatePasswordUseCase = ValidatePasswordUseCase(),
    private val checkUserLoggedIn: CheckUserLoggedInUseCase = CheckUserLoggedInUseCase()
):ViewModel() {
     var state:MutableState<LoginUIState> = mutableStateOf(LoginUIState())
        private set

    init {

        checkLogInStatus()
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
    }

}