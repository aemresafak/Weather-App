package com.example.weatherprojecttry_1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherprojecttry_1.ui.weather.cities.CitiesFragment
import com.example.weatherprojecttry_1.ui.weather.today.TodayFragment

class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> CitiesFragment()
            else -> TodayFragment()
        }

    }

}
