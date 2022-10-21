package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase

data class MainUIState(
    val loggedUserOut:Boolean = false
        )

class MainViewModel(
   private val logoutUseCase: LogoutUseCase = LogoutUseCase()
):ViewModel() {
    var state: MutableState<MainUIState> = mutableStateOf(MainUIState())
    private set

    fun signUserOut(){
        state.value = state.value.copy(loggedUserOut = logoutUseCase.invoke())
    }


}