package com.example.weatherprojecttry_1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import com.example.weatherprojecttry_1.data.currentWeather.MyViewModel
import com.example.weatherprojecttry_1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        if (it) {
            getLocation()
        }
        else {
            Toast.makeText(
                this,
                "The app will not fetch the weather at your location!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        addObservers()
        setSearchViewListener()
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Snackbar.make(
                    binding.root,
                    "Permission is needed to display weather info around your location.",
                    BaseTransientBottomBar.LENGTH_LONG
                ).apply {
                    val callback = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            requestPermissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }
                    }
                    addCallback(callback)
                    show()
                }
            }
            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)


        }
    }

    private fun getLocation() {
        Log.d(TAG, "getLocation: getting location...")
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