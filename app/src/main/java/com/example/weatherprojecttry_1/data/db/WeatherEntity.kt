package com.example.weatherprojecttry_1.data.db

import androidx.room.Embedded
import androidx.room.Entity
import com.example.weatherprojecttry_1.data.common.Location
import com.example.weatherprojecttry_1.data.common.Request

@Entity(tableName = "weather", primaryKeys = ["name"])
data class WeatherEntity(
    @Embedded
    val location: Location,
    @Embedded
    val currentWeather: CurrentWeather,
    @Embedded
    val request: Request
)