package com.example.weatherprojecttry_1.data.network

import com.example.weatherprojecttry_1.data.network.interceptors.ConnectivityInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherStackAPI {

    @GET("current")
    suspend fun fetchCurrentWeather(@Query("query") city: String) : Response<CurrentWeatherResponse>

    companion object {
        fun instantiate(
            keyInterceptor: Interceptor,
            connectivityInterceptor: Interceptor
        ): WeatherStackAPI {
            val client = OkHttpClient.Builder()
                .addInterceptor(keyInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl("http://api.weatherstack.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(WeatherStackAPI::class.java)
        }
    }

}