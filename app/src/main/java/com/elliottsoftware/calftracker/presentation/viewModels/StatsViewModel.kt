package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.statsCalf.calf
import com.elliottsoftware.calftracker.presentation.components.util.DataPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DataPoints {
    val dataPoints1 = listOf(
        DataPoint(0f, 0f, listOf(calf)),
        DataPoint(1f, 2f, listOf(calf, calf)),
        DataPoint(2f, 1f, listOf(calf)),
        DataPoint(3f, 1f, listOf(calf, calf)),
        DataPoint(4f, 5f, listOf(calf)),
        DataPoint(5f, 2f, listOf(calf, calf)),
        DataPoint(6f, 0f, listOf(calf)),
        DataPoint(7f, 0f, listOf(calf, calf)),
        DataPoint(8f, 0f, listOf(calf)),
        DataPoint(9f, 1f, listOf(calf, calf)),
        DataPoint(10f, 1f, listOf(calf)),
        DataPoint(11f, 3f, listOf(calf, calf)),
    )



}

data class StatsUiState(

    val calfList: List<FireBaseCalf> = listOf(),
    val graphData: Response<List<DataPoint>> = Response.Loading

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

