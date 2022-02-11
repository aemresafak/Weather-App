package com.example.weatherprojecttry_1

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import com.example.weatherprojecttry_1.data.currentWeather.MyViewModel
import com.example.weatherprojecttry_1.databinding.ActivityMainBinding


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        addObservers()
        setSearchViewListener()
    }

    private fun addObservers() {
        viewModel.getWeatherLiveData().observe(this) {
            if (it != null) {
                updateUI(it)
            }
        }
        viewModel.getLiveDataBitmap().observe(this) {
            if (it != null)
                binding.imageView.setImageBitmap(it)
        }
    }

    private fun setSearchViewListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query == null)
                    false
                else {
                    viewModel.fetchLiveData(query)
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })
    }

    private fun updateUI(currentWeather: CurrentWeather) {
        binding.textViewCity.text = currentWeather.location.name
        binding.textViewCountry.text = currentWeather.location.country
        val tempText = currentWeather.current.temperature.toString() + "°C"
        binding.textViewTemperature.text = tempText
        val humidityText = "Humidity is ${currentWeather.current.humidity} percent."
        binding.textViewHumidity.text = humidityText
        val feelsLikeText = "Feels like ${currentWeather.current.feelslike} °C."
        binding.textViewFeelsLike.text = feelsLikeText
    }









}