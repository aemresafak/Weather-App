package com.example.weatherprojecttry_1.data.network

import android.content.Context
import android.widget.Toast
import com.example.weatherprojecttry_1.data.network.models.CurrentWeatherResponse
import com.example.weatherprojecttry_1.utils.NoConnectionException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "WeatherNetworkDataSourc"
class WeatherNetworkDataSource @Inject constructor(private val api: WeatherStackAPI, @ApplicationContext val context: Context) {

    suspend fun fetchWeather(city: String): CurrentWeatherResponse? {
        return try {
            val response = api.fetchCurrentWeather(city)
            response.body()
        } catch (e: NoConnectionException) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            null
        }
    }

}