package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.remote.WeatherApi
import com.elliottsoftware.calftracker.data.remote.WeatherRetrofitInstance
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    val api: WeatherApi
): WeatherRepository {
    override suspend fun getWeather(lat: Double, long: Double) = flow {
        emit(Response.Loading)
        Timber.tag("WEATHERS").d("LOADING")

        try {
            val data = api.getWeatherData(lat, long).body()!!
            Timber.tag("WEATHERS").d("WEATHER DATA BELOW")


            val weatherList: MutableList<WeatherViewData> = mutableListOf()
            for (item in data.hourly.time.withIndex()) {
                weatherList.add(
                    WeatherViewData(
                        time = item.value,
                        temperature = data.hourly.temperature_2m[item.index]
                    )
                )
            }
            Timber.tag("WEATHERS").d(weatherList.toString())


            emit(Response.Success(weatherList))
        } catch (e: Exception) {
            Timber.tag("WEATHERS").e(e)
            emit(Response.Failure(Exception("FAILURE ON THE WEATHER CALL")))
        }
    }


   // override suspend fun getWeathers() = FlowCollector()

}