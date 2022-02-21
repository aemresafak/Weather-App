package com.example.weatherprojecttry_1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.weatherprojecttry_1.R
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity
import com.example.weatherprojecttry_1.databinding.FragmentWeatherBinding
import com.example.weatherprojecttry_1.utils.getDirectionFromAbbr

/**
 * Base fragment that has the fragment weather layout
 * This fragment is created in order to reduce boilerplate code
 * that TodayFragments and DetailFragments share
 */
open class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * Updates the UI with the information from fetched weather
     */
    protected fun updateUI(weatherEntity: WeatherEntity) {
        binding.apply {
            progressBar.visibility = View.INVISIBLE

            textViewUpdate.visibility = View.INVISIBLE

            textViewCity.text = weatherEntity.location.name

            textViewCountry.text = weatherEntity.location.country

            weatherEntity.currentWeather.apply {

                textViewTemperature.text = getString(R.string.temperature, temperature)

                textViewHumidity.text = getString(R.string.humidity, humidity)

                textViewFeelsLike.text = getString(R.string.feelslike, feelslike)

                textViewWindDirection.text =
                    getString(
                        R.string.windDirection,
                        getDirectionFromAbbr(this@WeatherFragment.requireContext(), windDir)
                    )

                textViewWindSpeed.text = getString(R.string.windSpeed, windSpeed)
            }
        }

        Glide.with(requireContext())
            .load(weatherEntity.currentWeather.weatherIcon)
            .circleCrop()
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}