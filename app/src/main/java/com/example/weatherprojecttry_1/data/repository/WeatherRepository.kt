package com.example.weatherprojecttry_1.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherprojecttry_1.data.db.WeatherDatabase
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.data.network.WeatherNetworkDataSource
import com.example.weatherprojecttry_1.utils.LocationNotFoundException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherNetworkDataSource: WeatherNetworkDataSource, private val weatherDatabase: WeatherDatabase
) {
    private val dao = weatherDatabase.getDao()

    private suspend fun saveToDatabase(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    fun loadAllWeathers(): LiveData<List<WeatherEntity>> = dao.getAllWeathers()

    suspend fun deleteWeather(weather: WeatherEntity) {
        dao.deleteWeather(weather)
    }

    suspend fun fetchDataFromNetwork(city: String): WeatherEntity {
        val weather = weatherNetworkDataSource.fetchWeather(city)
        if (weather?.location != null) {
            saveToDatabase(weather.toWeatherEntity())
            return weather.toWeatherEntity()
        }
        throw LocationNotFoundException()
    }

}