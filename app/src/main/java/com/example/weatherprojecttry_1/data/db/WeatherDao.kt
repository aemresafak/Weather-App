package com.example.weatherprojecttry_1.data.db

import androidx.room.Dao
import androidx.room.Insert
import com.example.weatherprojecttry_1.data.models.CurrentWeatherResponse

@Dao
interface WeatherDao {
    @Insert
    fun insertWeather(vararg weathers: CurrentWeatherResponse)


}