package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.data.remote.SmallWeatherDTO
import com.elliottsoftware.calftracker.data.remote.WeatherDataDto
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.GetWeatherUseCase
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class WeatherUiState(
    val weatherData: Response<SmallWeatherDTO> = Response.Loading,

    )

class WeatherViewModel(
    val getWeatherUseCase: GetWeatherUseCase = GetWeatherUseCase()
):ViewModel() {

    private val _uiState = mutableStateOf(WeatherUiState())
    val uiState: State<WeatherUiState> = _uiState




    public fun getWeatherData() =viewModelScope.launch{
        getWeatherUseCase(33.4,55.4).collect(
            FlowCollector {  response ->

                _uiState.value = _uiState.value.copy(weatherData = response)
            }
        )

    }



}