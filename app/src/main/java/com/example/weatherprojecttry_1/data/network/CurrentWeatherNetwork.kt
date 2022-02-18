package com.example.weatherprojecttry_1.data.network


import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.example.weatherprojecttry_1.data.db.CurrentWeather
import com.google.gson.annotations.SerializedName

data class CurrentWeatherNetwork(
    val feelslike: Int,
    val humidity: Int,
    val precip: Double,
    val pressure: Int,
    val temperature: Int,
    val visibility: Int,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Int
) {
    fun toCurrentWeather(): CurrentWeather = CurrentWeather(
        feelslike,
        humidity,
        precip,
        pressure,
        temperature,
        visibility,
        weatherIcons[0],
        windDir,
        windSpeed
    )
}