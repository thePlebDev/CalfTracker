package com.elliottsoftware.calftracker.data.remote



data class DailyWeatherDataDto(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
)