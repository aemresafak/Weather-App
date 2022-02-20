package com.example.weatherprojecttry_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherprojecttry_1.data.network.CurrentWeatherResponse

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getDao(): WeatherDao
}
