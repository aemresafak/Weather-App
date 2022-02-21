package com.example.weatherprojecttry_1.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherprojecttry_1.ui.fragments.cities.list.CityListFragment
import com.example.weatherprojecttry_1.ui.fragments.cities.detail.CityDetailFragment
import com.example.weatherprojecttry_1.ui.fragments.today.TodayFragment

class PagerAdapter(activity: FragmentActivity, val fragment: TodayFragment) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragment
            1 -> CityListFragment()
            else -> CityDetailFragment()
        }

    }

}
