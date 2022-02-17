package com.example.weatherprojecttry_1.data

import com.example.weatherprojecttry_1.data.apiresponse.CurrentWeatherResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface WeatherStackAPI {

    @GET("current")
    suspend fun fetchCurrentWeather(@Query("query") city: String) : Response<CurrentWeatherResponse>


}