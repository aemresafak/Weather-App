package com.example.weatherprojecttry_1.data.db.entities


data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)