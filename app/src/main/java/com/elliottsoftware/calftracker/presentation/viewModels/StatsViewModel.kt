package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class StatsViewModel: ViewModel() {



    private val _uiState = mutableStateOf("NOTHING SELECTED") // used in the viewModel
    val uiState: State<String> = _uiState //exposed to the components


    fun changeText(text:String){
        _uiState.value = text
    }
}

