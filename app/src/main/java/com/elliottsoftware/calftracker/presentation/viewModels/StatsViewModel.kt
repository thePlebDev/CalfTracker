package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.GetCalvesUseCase
import com.elliottsoftware.calftracker.presentation.components.statsCalf.calf
import com.elliottsoftware.calftracker.presentation.components.util.DataPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object DataPoints {
    val dataPoints1 = listOf(
        DataPoint(0f, 0f, mutableListOf(calf)),
        DataPoint(1f, 2f, mutableListOf(calf, calf)),
        DataPoint(2f, 1f, mutableListOf(calf)),
        DataPoint(3f, 1f, mutableListOf(calf, calf)),
        DataPoint(4f, 5f, mutableListOf(calf)),
        DataPoint(5f, 2f, mutableListOf(calf, calf)),
        DataPoint(6f, 0f, mutableListOf(calf)),
        DataPoint(7f, 0f, mutableListOf(calf, calf)),
        DataPoint(8f, 0f, mutableListOf(calf)),
        DataPoint(9f, 1f, mutableListOf(calf, calf)),
        DataPoint(10f, 1f, mutableListOf(calf)),
        DataPoint(11f, 3f, mutableListOf(calf, calf)),
    )



}

data class StatsUiState(

    val calfList: List<FireBaseCalf> = listOf(),
    val graphData: Response<List<DataPoint>> = Response.Loading

)


class StatsViewModel(
    //private val getCalvesUseCase: GetCalvesUseCase
): ViewModel() {



//    private val _uiState = mutableStateOf("NOTHING SELECTED") // used in the viewModel
//    val uiState: State<String> = _uiState //exposed to the components

    private val _uiState = mutableStateOf(StatsUiState())
    val uiState: State<StatsUiState> = _uiState


    fun changeListUI(calfList: List<FireBaseCalf>){
        _uiState.value = _uiState.value.copy(calfList = calfList)
    }

//    fun getCalves() = viewModelScope.launch(){
//        getCalvesUseCase.invoke().collect{response ->
//            _uiState.value = _uiState.value.copy(data = response)
//
//        }
//
//    }
}

