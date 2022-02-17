package com.example.weatherprojecttry_1.data.repository

import com.example.weatherprojecttry_1.data.WeatherStackAPI
import com.example.weatherprojecttry_1.data.apiresponse.CurrentWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "Repository"
private const val ACCESS_KEY = "02bcea2ebb2f23b9542aedfd532a82fa"
object Repository {


    private fun createAPI(): WeatherStackAPI {
        // create the interceptor
        val requestInterceptor = Interceptor() {
            val url = it.request()
                .url()
                .newBuilder()
                .addQueryParameter("access_key", ACCESS_KEY)
                .build()

            it.proceed(it.request().newBuilder()
                .url(url)
                .build())
        }

        // create custom client
        val client = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()

        // create api
        return Retrofit.Builder()
            .baseUrl("http://api.weatherstack.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherStackAPI::class.java)

    }
    private val api = createAPI()


    suspend fun fetchWeather(
        city: String
    ): CurrentWeatherResponse? {
        val response = api.fetchCurrentWeather(city)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }


}