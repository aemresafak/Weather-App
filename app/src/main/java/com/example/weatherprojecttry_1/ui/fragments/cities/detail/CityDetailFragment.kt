package com.example.weatherprojecttry_1.ui.fragments.cities.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.weatherprojecttry_1.ui.fragments.WeatherFragment
import com.example.weatherprojecttry_1.ui.fragments.cities.CitiesViewModel

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