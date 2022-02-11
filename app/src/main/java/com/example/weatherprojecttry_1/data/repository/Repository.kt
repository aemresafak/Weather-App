package com.example.weatherprojecttry_1.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
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


    suspend fun updateImage(liveData: MutableLiveData<Bitmap?>, url: String) {
        val response = api.fetchImage(url)
        liveData.postValue(BitmapFactory.decodeStream(response.body()?.byteStream()))
    }

    suspend fun updateWeather(liveData: MutableLiveData<CurrentWeather?>, city: String) {
        val response = api.fetchCurrentWeather(city)
        if (response.isSuccessful) {
            liveData.postValue(response.body())
        }
        else
            Log.d(TAG, "getWeather: error occured.")

    }


}