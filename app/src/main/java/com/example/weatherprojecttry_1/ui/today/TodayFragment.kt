package com.example.weatherprojecttry_1.ui.today

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.weatherprojecttry_1.MainActivity
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TodayFragment"
private const val REQUEST_CHECK_SETTINGS = 0
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
    private val viewModel: TodayViewModel by viewModels()

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
        task.addOnSuccessListener {
            if (it == null)
                showLocationErrorToast()
            else {
                val address = Geocoder(context).getFromLocation(it.latitude, it.longitude, 1)[0]
            }
        }
        task.addOnFailureListener {
            showLocationErrorToast()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}