package com.dvt.weatherapp.room

import androidx.room.TypeConverter
import com.dvt.weatherapp.models.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromString(value: String?): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Weather>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toDouble(value: String): Double {
        return value.toDouble()
    }

    @TypeConverter
    fun doubleToString(value: Double): String {
        return "$value"
    }
}