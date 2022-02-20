package com.example.weatherprojecttry_1.data.common


data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)