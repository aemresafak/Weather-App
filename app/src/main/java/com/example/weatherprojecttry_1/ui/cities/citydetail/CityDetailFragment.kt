package com.example.weatherprojecttry_1.ui.cities.citydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weatherprojecttry_1.R
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.databinding.FragmentCityDetailBinding
import com.example.weatherprojecttry_1.ui.cities.CitiesViewModel

class CityDetailFragment : Fragment() {

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!

    val viewModel: CitiesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mutableLiveDataClickedWeather.observe(viewLifecycleOwner) {
            if (it != null) {
                updateUI(it)
            }
        }

    }

    private fun updateUI(weatherEntity: WeatherEntity) {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            textViewUpdate.visibility = View.INVISIBLE
            textViewMoreInfo.text = "More info: "
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