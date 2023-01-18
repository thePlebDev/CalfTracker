package com.elliottsoftware.calftracker.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.DeleteCalfUseCase
import com.elliottsoftware.calftracker.domain.useCases.GetCalvesUseCase
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject



data class MainUIState(
    val loggedUserOut:Boolean = false,
    val data:Response<List<FireBaseCalf>> = Response.Loading,
    val darkTheme:Boolean = false,
    val searchableList:List<FireBaseCalf> = listOf()
        )

@HiltViewModel
class MainViewModel @Inject constructor(
   private val logoutUseCase: LogoutUseCase,
   private val getCalvesUseCase: GetCalvesUseCase,
   private val deleteCalfUseCase: DeleteCalfUseCase

):ViewModel() {
    var state: MutableState<MainUIState> = mutableStateOf(MainUIState())
    private set
    init{
        getCalves()
    }

    fun signUserOut(){
        state.value = state.value.copy(loggedUserOut = logoutUseCase.invoke())

    }


    private fun getCalves() = viewModelScope.launch(){
        getCalvesUseCase.invoke().collect{response ->

            state.value = state.value.copy(data = response)

        }

    }
    fun deleteCalf(id:String) = viewModelScope.launch{
        deleteCalfUseCase.invoke(id).collect{
        }


    }
    fun setDarkMode(){
        state.value = state.value.copy(darkTheme = !state.value.darkTheme)
    }



    fun searchCalfListByTag(tagNumber:String) = viewModelScope.launch{
        getCalvesUseCase.getCalfByTagNumber(tagNumber).collect{ response ->
            state.value = state.value.copy(data = response)
        }

    }




}