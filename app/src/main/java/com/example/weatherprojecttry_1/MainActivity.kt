package com.example.weatherprojecttry_1

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherprojecttry_1.data.currentWeather.CurrentWeather
import com.example.weatherprojecttry_1.data.currentWeather.MyViewModel
import com.example.weatherprojecttry_1.ui.AboutFragment
import com.example.weatherprojecttry_1.ui.TodayFragment
import com.example.weatherprojecttry_1.ui.SearchFragment
import com.example.weatherprojecttry_1.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import java.sql.ClientInfoStatus


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavView()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]


        viewModel.getWeatherLiveData().observe(this) {
            updateSearchFragment(it)
        }

        viewModel.getLiveDataBitmap().observe(this) {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as SearchFragment
            fragment.binding.imageViewWeather.setImageBitmap(it!!)
        }
    }
    
    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

    }

    private fun updateSearchFragment(currentWeather: CurrentWeather?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as SearchFragment
        fragment.binding.textViewCity.text = currentWeather?.location?.name
        fragment.binding.textViewCountry.text = currentWeather?.location?.country
        fragment.binding.textViewTemperature.text = currentWeather?.current?.temperature.toString()
        fragment.binding.textViewFeelslike.text = "Feels like: ${currentWeather?.current?.feelslike.toString()}"
        fragment.binding.textViewHumidity.text = "Humidity: ${currentWeather?.current?.humidity.toString()}"
    }




    // sets listener and initial item
    private fun initBottomNavView() {
        binding.bottomNavigationView.selectedItemId = R.id.menuItem7Days
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuItemToday -> replaceFragment(TodayFragment::class.java)
                R.id.menuItem7Days -> replaceFragment(SearchFragment::class.java)
                else -> replaceFragment(AboutFragment::class.java)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startTrackingLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,15000F) {
        }
    }


    //utility function to replace fragments
    private fun replaceFragment(fragment: Class<out Fragment>) : Boolean {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace(R.id.fragmentContainerView,fragment,null)
            commit()
        }
        return true
    }
}