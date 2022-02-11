package com.example.weatherprojecttry_1.data.currentWeather


import com.google.gson.annotations.SerializedName

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)