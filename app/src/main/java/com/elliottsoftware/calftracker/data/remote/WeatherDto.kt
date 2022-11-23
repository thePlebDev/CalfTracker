package com.elliottsoftware.calftracker.data.remote

import com.squareup.moshi.Json

data class WeatherDto(

    @field:Json(name = "hourly")
    val hourly:WeatherDataDto
)