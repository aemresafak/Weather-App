package com.example.weatherprojecttry_1.data.db.models


data class CurrentWeather(
    val feelslike: Int,
    val humidity: Int,
    val precip: Double,
    val pressure: Int,
    val temperature: Int,
    val visibility: Int,
    val weatherIcon: String,
    val windDir: String,
    val windSpeed: Int
)