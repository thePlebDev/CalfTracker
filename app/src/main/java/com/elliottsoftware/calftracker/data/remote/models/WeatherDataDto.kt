package com.elliottsoftware.calftracker.data.remote.models

import com.squareup.moshi.Json

data class WeatherDataDto(
    val time: List<String>,
    val temperature_2m: List<Double>,
)