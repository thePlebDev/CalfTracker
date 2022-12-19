package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.remote.WeatherApi
import com.elliottsoftware.calftracker.data.remote.WeatherRetrofitInstance
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import com.elliottsoftware.calftracker.domain.weather.WeatherViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    val api: WeatherApi
): WeatherRepository {
    override suspend fun getWeather(lat: Double, long: Double) = flow {
       emit(Response.Loading)
        Log.d("WEATHERS","LOADING")

        try {
            val data = api.getWeatherData(52.2,13.48).body()!!
            Log.d("WEATHERS","WEATHER DATA BELOW")

            val weatherList:MutableList<WeatherViewData> = mutableListOf()
            for(item in data.hourly.time.withIndex()){
                weatherList.add(
                    WeatherViewData(
                        time=item.value,
                        temperature = data.hourly.temperature_2m[item.index]
                    )
                )
            }


            emit(Response.Success(weatherList))
        }catch (e:Exception){
            Log.d("WEATHERS",e.message.toString())
            emit(Response.Failure(Exception("FAILURE ON THE WEATHER CALL")))
        }
    }


   // override suspend fun getWeathers() = FlowCollector()

}