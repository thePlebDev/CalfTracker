package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.GetCalvesUseCase
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import kotlinx.coroutines.launch

data class MainUIState(
    val loggedUserOut:Boolean = false,
    val data:Response<List<FireBaseCalf>> = Response.Loading
        )

class MainViewModel(
   private val logoutUseCase: LogoutUseCase = LogoutUseCase(),
    private val getCalvesUseCase: GetCalvesUseCase = GetCalvesUseCase()

):ViewModel() {
    var state: MutableState<MainUIState> = mutableStateOf(MainUIState())
    private set
    init{
        getCalves()
    }

    fun signUserOut(){
        state.value = state.value.copy(loggedUserOut = logoutUseCase.invoke())
    }


    fun getCalves() = viewModelScope.launch{
        getCalvesUseCase.invoke().collect{response ->
            state.value = state.value.copy(data = response)

        }

    }


}