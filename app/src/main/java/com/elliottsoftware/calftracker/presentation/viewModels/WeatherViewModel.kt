package com.elliottsoftware.calftracker.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.data.remote.WeatherDto
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.GetWeatherUseCase
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import kotlinx.coroutines.launch


data class WeatherUiState(
    val weatherData: Response<MutableList<WeatherViewData>> = Response.Loading,
    val focusedWeatherData:WeatherViewData? = null


    )

class WeatherViewModel(
    val getWeatherUseCase: GetWeatherUseCase = GetWeatherUseCase()
):ViewModel() {

    private val _uiState = mutableStateOf(WeatherUiState())
    val uiState: State<WeatherUiState> = _uiState




    public fun getWeatherData() =viewModelScope.launch{
        getWeatherUseCase(33.4,55.4).collect{ response ->
           // Log.d("WEATHER",response.toString())

            _uiState.value = _uiState.value.copy(weatherData = response)
        }

    }




}