package com.elliottsoftware.calftracker.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m&timezone=EST&daily=temperature_2m_max")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Response<WeatherDto>
}

//NEW WEATHER API ENDPOINT
//https://api.open-meteo.com/v1/gem?latitude=45.8979&longitude=64.3683&daily=temperature_2m_max&timezone=EST