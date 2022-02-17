package com.example.weatherprojecttry_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherprojecttry_1.data.network.apiresponse.CurrentWeatherResponse

@Database(entities = [CurrentWeatherResponse::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getDao(): WeatherDao

    companion object {
        @Volatile
        var instance: WeatherDatabase? = null
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather.db"
        ).build()


        private operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}
