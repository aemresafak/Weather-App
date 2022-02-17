package com.example.weatherprojecttry_1.data

import com.example.weatherprojecttry_1.data.network.apiresponse.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherStackAPI {

    @GET("current")
    suspend fun fetchCurrentWeather(@Query("query") city: String) : Response<CurrentWeatherResponse>


}