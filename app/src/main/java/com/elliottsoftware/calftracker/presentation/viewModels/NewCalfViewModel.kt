package com.elliottsoftware.calftracker.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.models.Response

data class NewCalfUIState(
    val calfTag:String = "",
    val calfTagError:String? = null,
    val cowTagNumber:String= "",
    val cciaNumber:String ="",
    val description:String="",
    val birthWeight:String="",
    val sex:String="Bull"
)


class NewCalfViewModel:ViewModel() {

    private val _state = mutableStateOf(NewCalfUIState())
    val state = _state



    fun updateCalfTag(tagNumber:String){

        _state.value = _state.value.copy(calfTag = tagNumber)
    }
    fun updateCowTagNumber(tagNumber: String){
        _state.value = _state.value.copy(cowTagNumber = tagNumber)
    }
    fun updateCciaNumber(cciaNUmber: String){
        _state.value = _state.value.copy(cciaNumber = cciaNUmber)
    }
    fun updateDescription(description: String){
        _state.value = _state.value.copy(description = description)
    }
    fun updateBirthWeight(birthWeight: String){
        _state.value = _state.value.copy(birthWeight = birthWeight)
    }
}