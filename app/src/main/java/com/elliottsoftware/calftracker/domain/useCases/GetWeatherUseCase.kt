package com.elliottsoftware.calftracker.domain.useCases


import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetWeatherParams(
    val lat:Double,
    val long:Double,
)


class GetWeatherUseCase @Inject constructor(
    private val weatherRepositoryImpl: WeatherRepository
):UseCase<GetWeatherParams,Flow<Response<MutableList<WeatherViewData>>>>() {



    override suspend fun execute(params: GetWeatherParams): Flow<Response<MutableList<WeatherViewData>>> {
        return weatherRepositoryImpl.getWeather(params.lat,params.long);
    }
}