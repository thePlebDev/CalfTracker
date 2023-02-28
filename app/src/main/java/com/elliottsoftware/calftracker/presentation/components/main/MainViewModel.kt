package com.elliottsoftware.calftracker.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject



data class MainUIState(
    //val loggedUserOut:Boolean = false,
    val data:Response<List<FireBaseCalf>> = Response.Loading,
    val darkTheme:Boolean = false,
    val chipText:List<String> = listOf("TOTAL: 0 ","BULLS: 0","HEIFERS: 0","VACCINATED: 0"),

        )

@HiltViewModel
class MainViewModel @Inject constructor(
   private val logoutUseCase: LogoutUseCase,
   private val getCalvesUseCase: GetCalvesUseCase,
   private val deleteCalfUseCase: DeleteCalfUseCase,
   private val getCalfByTagNumberUseCase: GetCalfByTagNumberUseCase,

):ViewModel() {
    private var _uiState: MutableState<MainUIState> = mutableStateOf(MainUIState())
    val state = _uiState
    init{
        getCalves()
    }

    fun signUserOut()= viewModelScope.launch{
        //state.value = state.value.copy(loggedUserOut = logoutUseCase.invoke())
        logoutUseCase.execute(Unit)

    }


     fun getCalves() = viewModelScope.launch(){
        getCalvesUseCase.execute(Unit).collect{response ->

            _uiState.value = _uiState.value.copy(data = response)

        }

    }
    fun deleteCalf(id:String) = viewModelScope.launch{
        deleteCalfUseCase.execute(id).collect{ response ->

        }


    }
    fun setDarkMode(){
        _uiState.value = _uiState.value.copy(darkTheme = !_uiState.value.darkTheme)
    }



    fun searchCalfListByTag(tagNumber:String) = viewModelScope.launch{
        getCalfByTagNumberUseCase.execute(tagNumber).collect{ response ->
            _uiState.value = _uiState.value.copy(data = response)
        }

    }


    fun setChipText(calfList:List<FireBaseCalf>){
        val total = calfList.size
        val bulls = calfList.count{it.sex == "Bull"}
        val heifers = calfList.count{it.sex == "Heifer"}
        val vaccinated = calfList.count{it.vaccinelist != null && it.vaccinelist!!.isNotEmpty()}

        val chipTextList:List<String> = listOf("TOTAL: $total ","BULLS: $bulls","HEIFERS: $heifers","VACCINATED: $vaccinated")
        _uiState.value = _uiState.value.copy(chipText = chipTextList)
    }




}