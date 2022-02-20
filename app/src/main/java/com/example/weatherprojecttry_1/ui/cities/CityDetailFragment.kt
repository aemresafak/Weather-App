package com.example.weatherprojecttry_1.ui.cities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.weatherprojecttry_1.ui.cities.CitiesViewModel
import com.example.weatherprojecttry_1.ui.WeatherFragment

class CityDetailFragment : WeatherFragment() {

    private val viewModel: CitiesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mutableLiveDataClickedWeather.observe(viewLifecycleOwner) {
            if (it != null) {
                updateUI(it)
            }
        }
    }
}