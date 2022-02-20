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
import com.example.weatherprojecttry_1.utils.getDirectionFromAbbr

class CityDetailFragment : Fragment() {

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitiesViewModel by activityViewModels()

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
            textViewCity.text = weatherEntity.location.name
            textViewCountry.text = weatherEntity.location.country
            weatherEntity.currentWeather.apply {

                textViewTemperature.text = getString(R.string.temperature, temperature)

                textViewHumidity.text = getString(R.string.humidity, humidity)

                textViewFeelsLike.text = getString(R.string.feelslike, feelslike)

                textViewWindDirection.text =
                    getString(R.string.windDirection, getDirectionFromAbbr(this@CityDetailFragment.requireContext(),windDir))

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