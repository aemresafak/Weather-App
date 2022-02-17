package com.example.weatherprojecttry_1.data.models


data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)