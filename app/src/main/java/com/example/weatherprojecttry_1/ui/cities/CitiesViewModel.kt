package com.example.weatherprojecttry_1.ui.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    lateinit var liveDataWeatherList: LiveData<List<WeatherEntity>>

    init {
        viewModelScope.launch {
            liveDataWeatherList = repository.loadAllWeathers()
        }
    }

}