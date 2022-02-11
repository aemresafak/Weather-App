package com.example.weatherprojecttry_1.data.currentWeather

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherprojecttry_1.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
private const val DEFAULT_URL = "https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0001_sunny.png"
class MyViewModel : ViewModel() {
    private val mutableLiveDataCurrentWeather = MutableLiveData<CurrentWeather?>()
    fun getWeatherLiveData() : LiveData<CurrentWeather?> = mutableLiveDataCurrentWeather
    private val mutableLiveDataBitmap: MutableLiveData<Bitmap?> = MutableLiveData<Bitmap?>()
    fun getLiveDataBitmap(): LiveData<Bitmap?> = mutableLiveDataBitmap


     private suspend fun updateImage(url: String) {
        Repository.updateImage(mutableLiveDataBitmap, url)
     }

     fun fetchLiveData(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.updateWeather(mutableLiveDataCurrentWeather, city)
            updateImage(getWeatherLiveData().value?.current?.weatherIcons?.get(0) ?: DEFAULT_URL)
        }
    }



}