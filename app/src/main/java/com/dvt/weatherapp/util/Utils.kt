package com.dvt.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {

        fun getDayOfTheWeek(timestamp: Long): String {
            return SimpleDateFormat("EEEE", Locale.getDefault()).format(timestamp * 1000)
        }

        fun getUnderstandableDate(time: Long): String {
            val format = "HH:mm EEEE"
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            return sdf.format(Date(time * 1000))
        }

        fun formatTemperature(temp: Double): String {
            return "%.1f".format(temp) + "\u00B0"
        }

    }
}