package com.example.weatherprojecttry_1.data

import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

private const val ACCESS_KEY = "02bcea2ebb2f23b9542aedfd532a82fa"
private const val ACCESS_QUERY = "access_key=$ACCESS_KEY"
interface WeatherStackAPI {
    @GET("current?$ACCESS_QUERY")
    suspend fun fetchCurrentWeather(@Query("query") city: String) : Response<CurrentWeather>
    @GET
    suspend fun fetchImage(@Url url: String): Response<ResponseBody>

}