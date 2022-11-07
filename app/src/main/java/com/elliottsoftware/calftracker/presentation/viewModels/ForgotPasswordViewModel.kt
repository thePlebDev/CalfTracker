package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.ResetPasswordUseCase
import com.elliottsoftware.calftracker.domain.useCases.ValidateEmailUseCase
import kotlinx.coroutines.launch

data class ForgotPasswordUIState(
    val email:String = "",
    val emailError:String? = null,
    val resetPassword:Response<Boolean> = Response.Success(false)
)

class ForgotPasswordViewModel(
    val validateEmailUseCase: ValidateEmailUseCase = ValidateEmailUseCase(),
    val resetPasswordUseCase: ResetPasswordUseCase = ResetPasswordUseCase()

):ViewModel() {

    private val _uiState = mutableStateOf(ForgotPasswordUIState())
    val state: State<ForgotPasswordUIState> = _uiState


    fun updateEmail(text:String){
        _uiState.value = _uiState.value.copy(email = text)
    }

    //todo:still need to do the email validation checks
    fun validateEmailText() = viewModelScope.launch{
        val email = _uiState.value.email
//       val errorMessage = validateEmailUseCase(email)
//        _uiState.value = _uiState.value.copy(emailError = errorMessage)

            resetPasswordUseCase.invoke(email).collect{response ->

                _uiState.value = _uiState.value.copy(resetPassword = response)
            }



    }
}