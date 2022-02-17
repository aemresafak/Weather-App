package com.example.weatherprojecttry_1.data.apiresponse


import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeather,
    val location: Location,
    val request: Request
)