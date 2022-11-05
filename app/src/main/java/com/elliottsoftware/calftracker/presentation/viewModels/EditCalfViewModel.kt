package com.elliottsoftware.calftracker.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EditCalfUiState(
    val calfTagNumber:String ="",
    val testTextError:String? = null,
    val cowTagNumber:String = "",
    val description:String="",
    val cCIANUmber:String ="",
    val birthWeight:String="",
    val sex:String = "Bull",
    val firebaseId:String ="",
    val loggedUserOut:Boolean = false
)

class EditCalfViewModel(
    val logoutUseCase: LogoutUseCase = LogoutUseCase()
):ViewModel() {



    private val _uiState = mutableStateOf(EditCalfUiState())
    val uiState: State<EditCalfUiState> = _uiState

    fun setCalf(calf:FireBaseCalf){

        _uiState.value = _uiState.value.copy(
            calfTagNumber = calf.calfTag?:"",
            cowTagNumber = calf.cowTag?:"",
            description = calf.details?:"",
            cCIANUmber = calf.cciaNumber?:"",
            birthWeight = calf.birthWeight?:"",
            sex = calf.sex?:"Bull",
            firebaseId = calf.id?:""
        )

    }

    fun updateCalfTag(text:String){
        _uiState.value = _uiState.value.copy(calfTagNumber = text)
    }
    //TODO: ADD THE UPDATE METHODS AND THE CHECKBOXES
    fun updateCowTag(text:String){
        _uiState.value = _uiState.value.copy(cowTagNumber = text)
    }
    fun updateDescription(text:String){
        _uiState.value = _uiState.value.copy(description = text)
    }
    fun updateCCIANumber(text:String){
        _uiState.value = _uiState.value.copy(cCIANUmber = text)
    }
    fun updateBirthWeight(text:String){
        _uiState.value = _uiState.value.copy(birthWeight = text)
    }
    fun updateSex(text:String){
        _uiState.value = _uiState.value.copy(sex = text)
    }

    fun validateText(){
        if(_uiState.value.calfTagNumber.isNullOrBlank()){
            _uiState.value = _uiState.value.copy(testTextError = "Can not be blank")
        }else{
            _uiState.value = _uiState.value.copy(testTextError = null)
        }
    }

    fun signUserOut(){
        _uiState.value = _uiState.value.copy(loggedUserOut = logoutUseCase.invoke())
    }

}