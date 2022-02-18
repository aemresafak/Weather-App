package com.example.weatherprojecttry_1.ui.cities

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherprojecttry_1.R
import com.example.weatherprojecttry_1.data.db.WeatherEntity
import com.example.weatherprojecttry_1.databinding.RecyclerViewItemBinding
import javax.inject.Inject

class CitiesAdapter @Inject constructor(): RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {
    var weathers: List<WeatherEntity> = arrayListOf()
    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerViewItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return CitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val weatherEntity = weathers[position]
        holder.binding.apply {
            textViewCityItem.text = weatherEntity.location.name
            textViewTemperatureItem.text = weatherEntity.currentWeather.temperature.toString()
            if (position % 2 == 0)
                cardView.setCardBackgroundColor(Color.LTGRAY)
            Glide.with(imageViewItem.context)
                .load(weatherEntity.currentWeather.weatherIcon)
                .circleCrop()
                .into(imageViewItem)
        }
    }

    override fun getItemCount(): Int = weathers.count()
}