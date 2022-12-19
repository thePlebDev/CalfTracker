package com.elliottsoftware.calftracker.presentation.viewModels

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.data.remote.WeatherDto
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.useCases.GetWeatherUseCase
import com.elliottsoftware.calftracker.domain.useCases.LogoutUseCase
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import com.elliottsoftware.calftracker.util.LocationManagerUtil
import com.elliottsoftware.calftracker.util.locationFlow
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log



data class WeatherUiState(
    val weatherData: Response<MutableList<WeatherViewData>> = Response.Loading,
    val focusedWeatherData:WeatherViewData = WeatherViewData("Not selected",0.00),
    val currentCourseLocation: Response<Location> = Response.Loading,
    val loggedUserOut:Boolean = false,
    val darkMode:Boolean = false


    )

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val getWeatherUseCase: GetWeatherUseCase,
    private val logoutUseCase: LogoutUseCase
):ViewModel() {

    private val _uiState = mutableStateOf(WeatherUiState())
    val uiState: State<WeatherUiState> = _uiState
//    init {
//        getWeatherData()
//    }





    fun getWeatherData(data: Location) =viewModelScope.launch{
        getWeatherUseCase(data.latitude,data.longitude).collect{ response ->


            _uiState.value = _uiState.value.copy(weatherData = response)
        }

    }
    public fun setFocusedData(focusedWeatherData: WeatherViewData){

        _uiState.value = _uiState.value.copy(focusedWeatherData = focusedWeatherData)
    }

     fun getLocation(context: Context) = viewModelScope.launch{

        var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.locationFlow().collect{ location ->

            _uiState.value = _uiState.value.copy(currentCourseLocation = location)
        }

    }
    fun setDarkMode(){
        _uiState.value = _uiState.value.copy(darkMode = !_uiState.value.darkMode)
    }
    fun signUserOut(){
        _uiState.value = _uiState.value.copy(loggedUserOut = logoutUseCase.invoke())

    }



}