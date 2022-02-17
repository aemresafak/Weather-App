package com.example.weatherprojecttry_1.data.db


data class CurrentWeather(
    val feelslike: Int,
    val humidity: Int,
    val precip: Int,
    val pressure: Int,
    val temperature: Int,
    val visibility: Int,
    val weatherIcon: String,
    val windDir: String,
    val windSpeed: Int
)