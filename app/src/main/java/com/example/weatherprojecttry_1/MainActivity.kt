package com.example.weatherprojecttry_1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherprojecttry_1.data.RecentQueryProvider
import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import com.example.weatherprojecttry_1.data.currentWeather.MyViewModel
import com.example.weatherprojecttry_1.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "MainActivity"
private const val REQUEST_CHECK_SETTINGS = 0
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var recentSuggestions: SearchRecentSuggestions
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        if (it)
            requestGPS()
        else
            showLocationErrorToast()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        recentSuggestions = SearchRecentSuggestions(this, RecentQueryProvider.AUTHORITY, RecentQueryProvider.MODE)
        addObservers()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menuItemSearch)?.actionView as SearchView
        searchView.isIconifiedByDefault = false
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEARCH) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            recentSuggestions.saveRecentQuery(query,null)
            viewModel.fetchLiveData(query!!)
            binding.progressBar.visibility = View.VISIBLE
            binding.textViewUpdate.visibility = View.VISIBLE
        }
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
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewUpdate.visibility = View.VISIBLE
        val task = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,CancellationTokenSource().token)
        task.addOnSuccessListener {
            if (it == null)
                showLocationErrorToast()
            else {
                val geocoder = Geocoder(this)
                val address = geocoder.getFromLocation(it.latitude,it.longitude,1)[0]
                viewModel.fetchLiveData(address.locality ?: address.adminArea)
            }
        }
        task.addOnFailureListener {
            showLocationErrorToast()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_CHECK_SETTINGS) {
            when(requestCode) {
                Activity.RESULT_OK -> {
                    getLocation()
                }
                Activity.RESULT_CANCELED -> {
                    showLocationErrorToast()
                }
            }
        }

    }

    private fun addObservers() {
        viewModel.getWeatherLiveData().observe(this) {
            if (it != null) {
                if (binding.progressBar.visibility == View.VISIBLE)
                    binding.progressBar.visibility = View.INVISIBLE
                if (binding.textViewUpdate.visibility == View.VISIBLE)
                    binding.textViewUpdate.visibility = View.INVISIBLE
                updateUI(it)
            }
        }
        viewModel.getLiveDataBitmap().observe(this) {
            if (it != null)
                binding.imageView.setImageBitmap(it)
        }
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

    private fun showLocationErrorToast() {
        Toast.makeText(this, "Location weather can not be fetched.",Toast.LENGTH_LONG).show()
    }








}