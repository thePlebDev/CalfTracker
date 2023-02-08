package com.elliottsoftware.calftracker.presentation.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

data class NewCalfUIState(
    val calfTag:String = "",
    val calfTagError:String? = null,
    val cowTagNumber:String= "",
    val cciaNumber:String ="",
    val description:String="",
    val birthWeight:String="",
    val sex:String="Bull",
    val birthDate:Date = Date(),
    val calfSaved:Response<Boolean> = Response.Success(false),
    val loggedUserOut:Boolean = false
)

@HiltViewModel
class NewCalfViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository ,
    private val logoutUseCase: LogoutUseCase // THIS IS CAUSING IT TO CRASH
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
    fun signUserOut(){
        _state.value = _state.value.copy(loggedUserOut = logoutUseCase.invoke())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate(date: LocalDate){
        //val localDate = LocalDate.now()
        val convertedDate= Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timber.tag("CALENDAR").d(convertedDate.toString())
        _state.value = _state.value.copy(birthDate = convertedDate)

    }


    //todo: THIS MIGHT BE BETTER IN A USE_CASE
    fun submitCalf() = viewModelScope.launch{
        val state = _state.value
        if(state.calfTag.isBlank()){
            _state.value = state.copy(calfTagError = "Calf tag can not be blank")
        }else{

            val calf = FireBaseCalf(state.calfTag,
                state.cowTagNumber,
                state.cciaNumber,state.sex,state.description, state.birthDate,state.birthWeight
            )
            databaseRepository.createCalf(calf).collect{ response ->
                _state.value = state.copy(calfSaved = response)
            }

        }
    }


}