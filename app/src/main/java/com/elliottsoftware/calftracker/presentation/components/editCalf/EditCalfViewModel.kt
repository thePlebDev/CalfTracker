package com.elliottsoftware.calftracker.presentation.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import com.elliottsoftware.calftracker.domain.useCases.UpdateCalfParams
import com.elliottsoftware.calftracker.domain.useCases.UpdateCalfUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
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
    val calfUpdated:Response<Boolean> = Response.Success(false),


    val vaccineText:String = "",
    val vaccineDate:String = Date().toString(),
    /***BELOW IS WHAT WILL COME FROM FIREBASE***/
    val vaccineList: SnapshotStateList<String> = mutableStateListOf<String>()
)

@HiltViewModel
class EditCalfViewModel @Inject constructor(
    val logoutUseCase: LogoutUseCase,
    val updateCalfUseCase: UpdateCalfUseCase
):ViewModel() {



    private val _uiState = mutableStateOf(EditCalfUiState())
    val uiState: State<EditCalfUiState> = _uiState
    private val auth: FirebaseAuth = Firebase.auth

    fun setCalf(calf:FireBaseCalf){

        val updatedList = mutableStateListOf<String>()
        calf.vaccinelist?.let{ calfVaccineList ->
            for (vaccine in calfVaccineList){
                updatedList.add(vaccine)
            }
        }
        _uiState.value = _uiState.value.copy(
            calfTagNumber = calf.calftag?:"",
            cowTagNumber = calf.cowtag?:"",
            description = calf.details?:"",
            cCIANUmber = calf.ccianumber?:"",
            birthWeight = calf.birthweight?:"",
            sex = calf.sex?:"Bull",
            birthDate= calf.date,
            firebaseId = calf.id?:"",
            vaccineList = updatedList

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

    fun signUserOut() = viewModelScope.launch{
        _uiState.value = _uiState.value.copy(loggedUserOut = logoutUseCase.execute(Unit))
    }

    private fun updateCalf()= viewModelScope.launch {
        val state = _uiState.value
        val vaccineList:MutableList<String> = mutableListOf()
        for(item in state.vaccineList){
            vaccineList.add(item)
        }
       val fireBaseCalf = FireBaseCalf(
           calftag = state.calfTagNumber,
           cowtag = state.cowTagNumber,
           ccianumber = state.cCIANUmber,
           sex = state.sex,
           details = state.description,
           birthweight = state.birthWeight,
           date = state.birthDate,
           id = state.firebaseId,
           vaccinelist = vaccineList,

       )
        updateCalfUseCase.execute(
            UpdateCalfParams(
                calf = fireBaseCalf,
                userEmail = auth.currentUser?.email!!
            )
        ).collect{ response ->
            _uiState.value = _uiState.value.copy(calfUpdated = response)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate(date: LocalDate){

        val convertedDate= Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        _uiState.value = _uiState.value.copy(birthDate = convertedDate)

    }


fun updateCalfUpdatedStateToFalse(){
    _uiState.value = _uiState.value.copy(calfUpdated = Response.Success(false))
}

    /*****VACCINE STUFF*******/
    fun updateVaccineText(text:String){
        _uiState.value = _uiState.value.copy(vaccineText = text)
    }

    fun updateDateText(date: String){

        _uiState.value = _uiState.value.copy(vaccineDate = date)

    }
    fun addItemToVaccineList(item:String){
        _uiState.value.vaccineList.add(item)

    }
    fun removeItemToVaccineList(item:String){
        _uiState.value.vaccineList.remove(item)

    }



}