package com.example.weatherprojecttry_1.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg weathers: WeatherEntity)

    @Delete
    suspend fun deleteWeather(vararg weathers: WeatherEntity)

    @Query("SELECT * from WEATHER")
    fun getAllWeathers(): LiveData<List<WeatherEntity>>

}