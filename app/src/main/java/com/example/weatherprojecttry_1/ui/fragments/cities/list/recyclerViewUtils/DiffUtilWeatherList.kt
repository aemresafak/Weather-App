package com.example.weatherprojecttry_1.ui.fragments.cities.list.recyclerViewUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity

class DiffUtilWeatherList(
    private val oldList: List<WeatherEntity>,
    private val newList: List<WeatherEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.count()

    override fun getNewListSize(): Int = newList.count()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}