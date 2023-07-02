package com.elliottsoftware.calftracker.presentation.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.data.repositories.DatabaseRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.repositories.DatabaseRepository
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
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
    val loggedUserOut:Boolean = false,


    val vaccineText:String = "",
    val vaccineDate:String = Date().toString(),
    /***BELOW IS WHAT WILL COME FROM FIREBASE***/
    val vaccineList:MutableList<String> = mutableListOf()
)


@HiltViewModel
class NewCalfViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository ,
    private val logoutUseCase: LogoutUseCase // THIS IS CAUSING IT TO CRASH
):ViewModel() {


    private val _uiState = mutableStateOf(NewCalfUIState())
    val state:State<NewCalfUIState> = _uiState
    private val auth: FirebaseAuth = Firebase.auth

    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO



    fun updateCalfTag(tagNumber:String){

        _uiState.value = _uiState.value.copy(calfTag = tagNumber)
    }
    fun updateCowTagNumber(tagNumber: String){
        _uiState.value = _uiState.value.copy(cowTagNumber = tagNumber)
    }
    fun updateCciaNumber(cciaNUmber: String){
        _uiState.value = _uiState.value.copy(cciaNumber = cciaNUmber)
    }
    fun updateDescription(description: String){
        _uiState.value = _uiState.value.copy(description = description)
    }
    fun updateBirthWeight(birthWeight: String){
        _uiState.value = _uiState.value.copy(birthWeight = birthWeight)
    }
    fun updateSex(sex:String){
        _uiState.value = _uiState.value.copy(sex = sex)
    }
    fun signUserOut() = viewModelScope.launch{
        _uiState.value = _uiState.value.copy(loggedUserOut = logoutUseCase.execute(Unit))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate(date: LocalDate){
        //val localDate = LocalDate.now()
        val convertedDate= Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        _uiState.value = _uiState.value.copy(birthDate = convertedDate)

    }
    fun clearData(){
        _uiState.value = _uiState.value.copy(
            calfTag = "",
            calfTagError = null,
            cowTagNumber= "",
            cciaNumber="",
            description="",
            birthWeight="",
            sex="Bull"
        )
    }
    fun resetResponse(){
        _uiState.value = _uiState.value.copy(
            calfSaved= Response.Success(false)
        )
    }

    /*****VACCINE STUFF*******/
    fun updateVaccineText(text:String){
        _uiState.value = _uiState.value.copy(vaccineText = text)
    }

    fun updateDateText(date: String){

        _uiState.value = _uiState.value.copy(vaccineDate = date)

    }



    //todo: THIS MIGHT BE BETTER IN A USE_CASE
    fun submitCalf(vaccineList:List<String>) = viewModelScope.launch{
        val state = _uiState.value
        if(state.calfTag.isBlank()){
            _uiState.value = state.copy(calfTagError = "Calf tag can not be blank")
        }else{

            val calf = FireBaseCalf(
                calftag = state.calfTag,
                cowtag = state.cowTagNumber,
                ccianumber = state.cciaNumber,
                sex =state.sex,
                details = state.description,
                date = state.birthDate,
                birthweight = state.birthWeight,
                vaccinelist = vaccineList
            )
            databaseRepository.createCalf(calf,auth.currentUser?.email!!)
                .flowOn(dispatcherIO)
                .collect{ response ->
                _uiState.value = state.copy(calfSaved = response)
            }

        }
    }


}