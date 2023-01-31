package com.elliottsoftware.calftracker.domain.repositories

import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeather(lat:Double,long:Double): Flow<Response<MutableList<WeatherViewData>>>
}