package com.example.weatherprojecttry_1.ui.fragments.cities.list.recyclerViewUtils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherprojecttry_1.R
import com.example.weatherprojecttry_1.data.db.models.WeatherEntity
import com.example.weatherprojecttry_1.databinding.RecyclerViewItemBinding



class CitiesAdapter(val clickHandler: ItemClickHandler): RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {
    var weathers: List<WeatherEntity> = arrayListOf()
        set(value) {
            val diffUtil = DiffUtilWeatherList(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }
    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerViewItemBinding.bind(view)
        init {
            view.setOnClickListener {
                clickHandler.handleClick(weathers[adapterPosition])
            }
        }
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
            if (holder.layoutPosition % 2 == 0)
                cardView.setCardBackgroundColor(Color.LTGRAY)
            else
                cardView.setCardBackgroundColor(Color.WHITE)
            Glide.with(imageViewItem.context)
                .load(weatherEntity.currentWeather.weatherIcon)
                .circleCrop()
                .into(imageViewItem)
        }
    }

    override fun getItemCount(): Int = weathers.count()

    interface ItemClickHandler {
        fun handleClick(weatherEntity: WeatherEntity)
    }
}