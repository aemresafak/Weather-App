package com.example.weatherprojecttry_1.ui.today

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.data.repository.WeatherRepository
import com.example.weatherprojecttry_1.utils.LocationNotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val repository: WeatherRepository, @ApplicationContext val context: Context): ViewModel() {
    private val mutableLiveDataCurrentWeather = MutableLiveData<WeatherEntity?>()
    fun getLiveDataCurrentWeather(): LiveData<WeatherEntity?> = mutableLiveDataCurrentWeather

    fun fetchCurrentWeather(city: String) {
        viewModelScope.launch {
            try {
                val weather = repository.fetchDataFromNetwork(city)
                mutableLiveDataCurrentWeather.postValue(weather)
            } catch (e: LocationNotFoundException) {
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Location not found!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}