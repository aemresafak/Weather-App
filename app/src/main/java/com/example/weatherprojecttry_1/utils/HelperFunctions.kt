package com.example.weatherprojecttry_1.utils

import android.content.Context
import com.example.weatherprojecttry_1.R

/**
 * helper method that returns expansions of direction abbreviations
 */
fun getDirectionFromAbbr(context: Context, abbr: String): String {
    return when (abbr) {
        "N" -> context.getString(R.string.north)
        "E" -> context.getString(R.string.east)
        "S" -> context.getString(R.string.south)
        "W" -> context.getString(R.string.west)
        "NW" -> context.getString(R.string.northwest)
        "NE" -> context.getString(R.string.northeast)
        "SW" -> context.getString(R.string.southwest)
        else -> context.getString(R.string.southeast)
    }
}