package com.elliottsoftware.calftracker.domain.useCases


import com.elliottsoftware.calftracker.data.remote.WeatherDataDto
import com.elliottsoftware.calftracker.data.remote.WeatherDto
import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.data.repositories.WeatherRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import com.elliottsoftware.calftracker.domain.weather.WeatherData
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase(
    private val weatherRepositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()
) {

    suspend operator fun invoke(lat:Double, long:Double): Flow<Response<MutableList<WeatherViewData>>> {
        return weatherRepositoryImpl.getWeather(33.4,55.3);
    }
}