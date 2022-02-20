package com.example.weatherprojecttry_1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getDao(): WeatherDao
}
