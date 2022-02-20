package com.example.weatherprojecttry_1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getDao(): WeatherDao
}
