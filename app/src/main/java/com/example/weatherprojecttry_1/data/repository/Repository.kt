package com.example.weatherprojecttry_1.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.weatherprojecttry_1.MainActivity
import com.example.weatherprojecttry_1.data.WeatherStackAPI
import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "Repository"

object Repository {

    private val api = Retrofit.Builder()
        .baseUrl("http://api.weatherstack.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherStackAPI::class.java)


    suspend fun updateWeather(
        urlLiveData: MutableLiveData<String?>,
        weatherLiveData: MutableLiveData<CurrentWeather?>,
        city: String
    ) {
        val response = api.fetchCurrentWeather(city)
        if (response.isSuccessful) {
            weatherLiveData.postValue(response.body())
            urlLiveData.postValue(response.body()?.current?.weatherIcons?.get(0))
        } else
            Log.d(TAG, "getWeather: error occured.")

    }


}