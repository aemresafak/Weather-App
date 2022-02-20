package com.example.weatherprojecttry_1.data.network


import com.example.weatherprojecttry_1.data.common.Location
import com.example.weatherprojecttry_1.data.common.Request
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeatherNetwork,
    val location: Location,
    val request: Request
) {
    fun toWeatherEntity(): WeatherEntity = WeatherEntity(
        location,
        currentWeather.toCurrentWeather(),
        request
    )
}