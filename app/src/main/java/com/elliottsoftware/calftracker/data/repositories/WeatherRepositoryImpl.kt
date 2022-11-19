package com.elliottsoftware.calftracker.data.repositories

import android.util.Log
import com.elliottsoftware.calftracker.data.remote.SmallWeatherDTO
import com.elliottsoftware.calftracker.data.remote.WeatherApi
import com.elliottsoftware.calftracker.data.remote.WeatherRetrofitInstance
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    val api: WeatherApi = WeatherRetrofitInstance.api
): WeatherRepository {
    override suspend fun getWeather(lat: Double, long: Double)= flow {
        emit(Response.Loading)
        try {
            val data = api.getWeatherData(52.2,13.48).body()!!
            emit(Response.Success(data))
        }catch (e:Exception){
            Log.e("PostRepositoryImpException",e.message.toString())
            emit(Response.Failure(Exception("FAILURE ON THE WEATHER CALL")))
        }
    }


}