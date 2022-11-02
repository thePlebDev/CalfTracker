package com.elliottsoftware.calftracker.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import kotlinx.coroutines.launch
import java.util.*

data class NewCalfUIState(
    val calfTag:String = "",
    val calfTagError:String? = null,
    val cowTagNumber:String= "",
    val cciaNumber:String ="",
    val description:String="",
    val birthWeight:String="",
    val sex:String="Bull",
    val calfSaved:Response<Boolean> = Response.Success(false)
)


class NewCalfViewModel(
    val databaseRepository: DatabaseRepositoryImpl = DatabaseRepositoryImpl()
):ViewModel() {

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
    fun updateSex(sex:String){
        _state.value = _state.value.copy(sex = sex)
    }

    fun submitCalf() = viewModelScope.launch{
        val state = _state.value
        if(state.calfTag.isBlank()){
            _state.value = state.copy(calfTagError = "Calf tag can not be blank")
        }else{

            val calf = FireBaseCalf(state.calfTag,
                state.cowTagNumber,
                state.cciaNumber,state.sex,state.description, Date(),state.birthWeight
            )
            databaseRepository.createCalf(calf).collect{ response ->
                _state.value = state.copy(calfSaved = response)
            }

        }
    }


}