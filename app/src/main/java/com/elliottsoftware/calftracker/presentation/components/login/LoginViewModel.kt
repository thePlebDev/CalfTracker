package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.*
import com.elliottsoftware.calftracker.presentation.components.login.Credentials
import com.elliottsoftware.calftracker.presentation.components.login.Email
import com.elliottsoftware.calftracker.presentation.components.login.LoginViewState
import com.elliottsoftware.calftracker.presentation.components.login.Password
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
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
     private var _uiState:MutableState<LoginUIState> = mutableStateOf(LoginUIState())
     val state: State<LoginUIState> = _uiState

    //TODO: WE NEED TO GET IT SO WE CAN JUST TYPE FIRST. THEN WORRY ABOUT THE VALIDATION STUFF
    private val _viewState: MutableStateFlow<LoginViewState> =
        MutableStateFlow(LoginViewState.Initial)
    val viewState: StateFlow<LoginViewState> = _viewState

    init {

        checkLogInStatus()
    }

    fun emailChanged(email: String) {
        val currentCredentials = _viewState.value.credentials
        val currentPasswordErrorMessage =
            (_viewState.value as? LoginViewState.Active)?.passwordInputErrorMessage

        _viewState.value = LoginViewState.Active(
            credentials = currentCredentials.withUpdatedEmail(email),
            emailInputErrorMessage = null,
            passwordInputErrorMessage = currentPasswordErrorMessage,
        )
    }
    fun passwordChanged(password: String) {
        val currentCredentials = _viewState.value.credentials
        val currentEmailErrorMessage = (_viewState.value as? LoginViewState.Active)?.emailInputErrorMessage

        _viewState.value = LoginViewState.Active(
            credentials = currentCredentials.withUpdatedPassword(password),
            passwordInputErrorMessage = null,
            emailInputErrorMessage = currentEmailErrorMessage,
        )
    }

    private fun Credentials.withUpdatedPassword(password: String): Credentials {
        return this.copy(password = Password(password))
    }

    private fun Credentials.withUpdatedEmail(email: String): Credentials {
        return this.copy(email = Email(email))
    }


    //TODO: THIS NEEDS TO BE WORKED OUT
    private fun loginUser(email: String,password:String)= viewModelScope.launch{
        loginUseCase.execute(Credentials(Email(email), Password( password))).collect{ response ->
            _uiState.value = _uiState.value.copy(loginStatus = response)
        }
    }


    fun updateEmail(email: String){
        _uiState.value = _uiState.value.copy(email=email)

    }
    fun updatePassword(password:String){
        _uiState.value = _uiState.value.copy(password = password)
    }

    private fun checkLogInStatus() = viewModelScope.launch{
        val auth = checkUserLoggedIn.execute(Unit)

        _uiState.value = _uiState.value.copy(isUserLoggedIn = auth)

    }


    fun passwordIconChecked(checked:Boolean){
        _uiState.value = _uiState.value.copy(passwordIconChecked= checked)

    }


    fun submitButton(){
        val verifyEmail = validateEmail(_uiState.value.email)
        val verifyPassword = validatePassword(_uiState.value.password)
        _uiState.value = _uiState.value.copy(emailError = verifyEmail, passwordError = verifyPassword)

        if(_uiState.value.emailError == null && _uiState.value.passwordError == null){
            loginUser(_uiState.value.email,_uiState.value.password)
        }
    }

}