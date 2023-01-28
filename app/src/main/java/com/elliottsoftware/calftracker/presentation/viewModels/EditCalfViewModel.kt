package com.elliottsoftware.calftracker.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import com.elliottsoftware.calftracker.domain.useCases.UpdateCalfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class EditCalfUiState(
    val calfTagNumber:String ="",
    val testTextError:String? = null,
    val cowTagNumber:String = "",
    val description:String="",
    val cCIANUmber:String ="",
    val birthWeight:String="",
    val sex:String = "Bull",
    val birthDate:Date? = null,
    val firebaseId:String ="",
    val loggedUserOut:Boolean = false,
    val calfUpdated:Response<Boolean> = Response.Success(false)
)

@HiltViewModel
class EditCalfViewModel @Inject constructor(
    val logoutUseCase: LogoutUseCase,
    val updateCalfUseCase: UpdateCalfUseCase
):ViewModel() {



    private val _uiState = mutableStateOf(EditCalfUiState())
    val uiState: State<EditCalfUiState> = _uiState

    fun setCalf(calf:FireBaseCalf){

        _uiState.value = _uiState.value.copy(
            calfTagNumber = calf.calftag?:"",
            cowTagNumber = calf.cowtag?:"",
            description = calf.details?:"",
            cCIANUmber = calf.ccianumber?:"",
            birthWeight = calf.birthweight?:"",
            sex = calf.sex?:"Bull",
            birthDate= calf.date,
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
            updateCalf()
        }
    }

    fun signUserOut(){
        _uiState.value = _uiState.value.copy(loggedUserOut = logoutUseCase.invoke())
    }

    fun updateCalf()= viewModelScope.launch {
        val state = _uiState.value
       val fireBaseCalf = FireBaseCalf(
           calftag = state.calfTagNumber,
           cowtag = state.cowTagNumber,
           ccianumber = state.cCIANUmber,
           sex = state.sex,
           details = state.description,
           birthweight = state.birthWeight,
           date = state.birthDate,
           id = state.firebaseId
       )
        updateCalfUseCase(fireBaseCalf).collect{ response ->
            _uiState.value = _uiState.value.copy(calfUpdated = response)
        }
    }

}