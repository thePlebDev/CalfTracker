package com.elliottsoftware.calftracker.domain.useCases

import com.elliottsoftware.calftracker.data.remote.SmallWeatherDTO
import com.elliottsoftware.calftracker.data.remote.WeatherDataDto
import com.elliottsoftware.calftracker.data.repositories.AuthRepositoryImpl
import com.elliottsoftware.calftracker.data.repositories.WeatherRepositoryImpl
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.AuthRepository
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import com.elliottsoftware.calftracker.domain.weather.WeatherData
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase(
    private val weatherRepositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()
) {

    suspend operator fun invoke(lat:Double, long:Double): Flow<Response<SmallWeatherDTO>> {
        return weatherRepositoryImpl.getWeather(lat,long)
    }
}