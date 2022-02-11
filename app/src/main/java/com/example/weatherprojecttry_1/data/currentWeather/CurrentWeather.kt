package com.example.weatherprojecttry_1.data.currentWeather


import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val current: Current,
    val location: Location,
    val request: Request
)