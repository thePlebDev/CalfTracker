package com.elliottsoftware.calftracker.data.remote

data class SmallWeatherDTO(
    val latitude:Double,
    val longitude:Double,
    val generationtime_ms:Double,
    val utc_offset_seconds:Double,
    val timezone:String,
    val timezone_abbreviation:String,
    val elevation:Double
)

//"latitude": 52.2,
//"longitude": 13.48,
//"generationtime_ms": 0.18894672393798828,
//"utc_offset_seconds": 0,
//"timezone": "GMT",
//"timezone_abbreviation": "GMT",
//"elevation": 74.0