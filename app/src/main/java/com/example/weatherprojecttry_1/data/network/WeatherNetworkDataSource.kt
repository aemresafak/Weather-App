package com.example.weatherprojecttry_1.data.network

import android.util.Log
import com.example.weatherprojecttry_1.utils.NoConnectionException
import javax.inject.Inject

private const val TAG = "WeatherNetworkDataSourc"
class WeatherNetworkDataSource @Inject constructor(private val api: WeatherStackAPI) {

    suspend fun fetchWeather(city: String): CurrentWeatherResponse? {
        return try {
            val response = api.fetchCurrentWeather(city)
            response.body()
        } catch (e: NoConnectionException) {
            Log.d(TAG, "fetchWeather: no connection")
            null
        }
    }

}