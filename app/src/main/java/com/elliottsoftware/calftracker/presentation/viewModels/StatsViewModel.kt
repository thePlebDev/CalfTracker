package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class StatsUiState(

    val calfList: List<FireBaseCalf> = listOf()
)
class StatsViewModel: ViewModel() {



//    private val _uiState = mutableStateOf("NOTHING SELECTED") // used in the viewModel
//    val uiState: State<String> = _uiState //exposed to the components

    private val _uiState = mutableStateOf(StatsUiState())
    val uiState: State<StatsUiState> = _uiState


//    fun changeText(text:String){
//        _uiState.value = text
//    }
    fun changeListUI(calfList: List<FireBaseCalf>){
        _uiState.value = _uiState.value.copy(calfList = calfList)
    }
}

