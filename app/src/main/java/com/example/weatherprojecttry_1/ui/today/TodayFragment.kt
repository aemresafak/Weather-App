package com.example.weatherprojecttry_1.ui.today

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weatherprojecttry_1.MainActivity
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.databinding.FragmentTodayBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "TodayFragment"
@AndroidEntryPoint
class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            requestGPS()
        else
            showLocationErrorToast()
    }


    private val enableLocationSettingsLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {

            } else {
                showLocationErrorToast()
            }
        }
    private val viewModel: TodayViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveDataCurrentWeather().observe(viewLifecycleOwner) {
            if (it != null)
                updateUI(it)
            else {
                binding.apply {
                    textViewUpdate.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }


    /**
     * Helper method that shows a snackbar that
     * launches permission request when dismissed
     */
    private fun showRationaleSnackBar() {
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

    /**
     * Helper method displays an error toast
     */
    private fun showLocationErrorToast() {
        Toast.makeText(context, "Location weather can not be fetched.", Toast.LENGTH_LONG).show()
    }


    /**
     * Main method to request permission
     */
    private fun requestPermission() {
        when {
            // if permission has been already granted request gps
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestGPS()
            }
            // if a rationale needs to be shown, show a snackbar
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showRationaleSnackBar()
            }
            // if permission has not been granted yet ask for it
            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    /**
     * checks if gps is enabled
     * if not prompt user to enable it
     */
    private fun requestGPS() {
        val locationRequest = LocationRequest.create().apply {
            interval = 15000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(settingsRequest)


        task.addOnSuccessListener {
            getLocation()
        }

        task.addOnFailureListener(
            getGPSOnFailureListener()
        )
    }

    /**
     * prompts user to enable gps if it is not enabled
     */
    private fun getGPSOnFailureListener(): OnFailureListener {
        return OnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    enableLocationSettingsLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    /**
     * this method is called when permissions are granted and gps is enabled
     * gets the location as longitude and latitude, then converts them to an address using geocoder
     *
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val task = (activity as MainActivity).fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )
        showProgress()
        task.addOnSuccessListener {
            if (it == null)
                showLocationErrorToast()
            else {
                val address = Geocoder(context).getFromLocation(it.latitude, it.longitude, 1)[0]
                viewModel.fetchCurrentWeather(address.locality ?: address.adminArea)
            }
        }
        task.addOnFailureListener {
            showLocationErrorToast()
            binding.progressBar.visibility = View.INVISIBLE
            binding.textViewUpdate.visibility = View.INVISIBLE
        }
    }

    /**
     * utility method that shows a progress bar
     */
    private fun showProgress() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            textViewUpdate.visibility = View.VISIBLE
        }

    }


    /**
     * Updates the UI with the information from fetched weather
     */
    private fun updateUI(weatherEntity: WeatherEntity) {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            textViewUpdate.visibility = View.INVISIBLE
            textViewCity.text = weatherEntity.location.name
            textViewCountry.text = weatherEntity.location.country
            val tempText = weatherEntity.currentWeather.temperature.toString() + "°"
            textViewTemperature.text = tempText
            val humidityText = "Humidity is ${weatherEntity.currentWeather.humidity}."
            textViewHumidity.text = humidityText
            val feelsLikeText = "Feels like ${weatherEntity.currentWeather.feelslike}°."
            textViewFeelsLike.text = feelsLikeText
            val windDirectionText =
                "Wind direction is ${getDirectionFromAbbr(weatherEntity.currentWeather.windDir)}."
            textViewWindDirection.text = windDirectionText
            val windSpeedText = "Wind speed is ${weatherEntity.currentWeather.windSpeed} km/h."
            textViewWindSpeed.text = windSpeedText
            Glide.with(requireContext())
                .load(weatherEntity.currentWeather.weatherIcon)
                .circleCrop()
                .into(imageView)
        }
    }
    private fun getDirectionFromAbbr(abbr: String): String {
        return when (abbr) {
            "N" -> "north"
            "E" -> "east"
            "S" -> "south"
            "W" -> "west"
            "NW" -> "northwest"
            "NE" -> "northeast"
            "SW" -> "southwest"
            else -> "southeast"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}