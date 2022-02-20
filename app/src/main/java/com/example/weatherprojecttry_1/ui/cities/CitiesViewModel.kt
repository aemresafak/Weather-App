package com.example.weatherprojecttry_1.ui.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity
import com.example.weatherprojecttry_1.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
    val liveDataWeatherList: LiveData<List<WeatherEntity>> = repository.loadAllWeathers()

    val mutableLiveDataClickedWeather = MutableLiveData<WeatherEntity?>()


    fun deleteWeather(weatherEntity: WeatherEntity) {
        viewModelScope.launch {
            repository.deleteWeather(weatherEntity)
        }
    }

}