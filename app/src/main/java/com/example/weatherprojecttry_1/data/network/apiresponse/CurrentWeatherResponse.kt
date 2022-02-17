package com.example.weatherprojecttry_1.data.network.apiresponse


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherprojecttry_1.data.db.entities.CurrentWeather
import com.example.weatherprojecttry_1.data.db.entities.Location
import com.example.weatherprojecttry_1.data.db.entities.Request
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class CurrentWeatherResponse(
    @SerializedName("current")
    @Embedded
    val currentWeather: CurrentWeather,
    @Embedded
    val location: Location,
    @Embedded
    val request: Request
) {
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null
}