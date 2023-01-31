package com.elliottsoftware.calftracker.data.remote.models

import com.squareup.moshi.Json

data class WeatherDto(

    @field:Json(name = "hourly")
    val hourly: WeatherDataDto,

//    @field:Json(name = "daily")
//    val daily:DailyWeatherDataDto


)