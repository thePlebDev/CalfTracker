package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.DataPoint
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.models.fireBase.calfListToDataPointList
import com.elliottsoftware.calftracker.domain.useCases.GetCalvesUseCase
import com.elliottsoftware.calftracker.domain.useCases.GetDataPointUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

val calf = FireBaseCalf("22d2",
    "22d2",
    "22d2ddrew4r","Bull","STUFF AND THINGS", Date(),"222"
)
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


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getDataPointUseCase: GetDataPointUseCase
): ViewModel() {


    private val _uiState = mutableStateOf(StatsUiState())
    val uiState: State<StatsUiState> = _uiState

    init {
        getDataPoints()
    }


    fun changeListUI(calfList: List<FireBaseCalf>){
        _uiState.value = _uiState.value.copy(calfList = calfList)
    }


    private fun getDataPoints() = viewModelScope.launch(){
        getDataPointUseCase.invoke().collect{response ->

            _uiState.value = _uiState.value.copy(graphData = response)

        }

    }
    private fun getDataPointsTest(){
        _uiState.value = _uiState.value.copy(graphData = Response.Success(DataPoints.dataPoints1))

    }
}

