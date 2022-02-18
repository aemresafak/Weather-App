package com.example.weatherprojecttry_1.data.repository

import com.example.weatherprojecttry_1.data.db.WeatherDatabase
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.data.network.WeatherNetworkDataSource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherNetworkDataSource: WeatherNetworkDataSource, private val weatherDatabase: WeatherDatabase
) {
    private val dao = weatherDatabase.getDao()

    private suspend fun saveToDatabase(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    suspend fun fetchDataFromNetwork(city: String): WeatherEntity? {
        val weather = weatherNetworkDataSource.fetchWeather(city)
        if (weather != null) {
            saveToDatabase(weather.toWeatherEntity())
        }
        return weather?.toWeatherEntity()
    }

}