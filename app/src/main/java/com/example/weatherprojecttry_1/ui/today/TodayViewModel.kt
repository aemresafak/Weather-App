package com.example.weatherprojecttry_1.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
    private val mutableLiveDataCurrentWeather = MutableLiveData<WeatherEntity?>()
    fun getLiveDataCurrentWeather(): LiveData<WeatherEntity?> = mutableLiveDataCurrentWeather

    fun fetchCurrentWeather(city: String) {
        viewModelScope.launch {
            mutableLiveDataCurrentWeather.postValue(repository.fetchDataFromNetwork(city))
        }
    }


}