package com.example.weatherprojecttry_1.data.apiresponse


data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)