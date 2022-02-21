package com.example.weatherprojecttry_1.ui.fragments.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity
import com.example.weatherprojecttry_1.data.repository.WeatherRepository
import com.example.weatherprojecttry_1.utils.LocationNotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
    private val mutableLiveDataCurrentWeather = MutableLiveData<WeatherEntity?>()
    fun getLiveDataCurrentWeather(): LiveData<WeatherEntity?> = mutableLiveDataCurrentWeather

    /*
     * Flag is used to detect an occurrence of error
     * from the UI element that uses this view model
     * The value it contains is of no importance
     * Every time an error occurs, the live data is updated
     */
    private val mutableLiveDataFlag = MutableLiveData<Int?>().also {
        it.value = 0
    }
    fun getLiveDataFlag() : LiveData<Int?> = mutableLiveDataFlag

    fun fetchCurrentWeather(city: String) {
        viewModelScope.launch {
            try {
                val weather = repository.fetchDataFromNetwork(city)
                mutableLiveDataCurrentWeather.postValue(weather)
            } catch (e: LocationNotFoundException) {
                mutableLiveDataFlag.value = mutableLiveDataFlag.value!! + 1
            }
        }
    }


}