package com.example.weatherprojecttry_1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import com.example.weatherprojecttry_1.data.currentWeather.MyViewModel
import com.example.weatherprojecttry_1.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


private const val TAG = "MainActivity"
private const val REQUEST_CHECK_SETTINGS = 0
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        if (it) {
            requestGPS()
        }
        else {
            Toast.makeText(
                this,
                "The app can not fetch the weather at your location!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val enableGPSLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        Log.d(TAG, "isOkay: ${it.resultCode == Activity.RESULT_OK}")
        Log.d(TAG, "isCanceled: ${it.resultCode == Activity.RESULT_CANCELED}")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        addObservers()
        setSearchViewListener()
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }


    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestGPS()
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

    private fun requestGPS() {
        val locationRequest = LocationRequest.create().apply {
            interval = 15000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(settingsRequest)


        task.addOnSuccessListener {
            getLocation()
        }

        task.addOnFailureListener {
            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
        }

    }

    private fun getLocation() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_CHECK_SETTINGS) {
            when(requestCode) {
                Activity.RESULT_OK -> {
                    getLocation()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this, "App will not be able to get location weather.",Toast.LENGTH_LONG).show()
                }
            }
        }

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