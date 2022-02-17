package com.dvt.weatherapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Wind(
    var speed: Double = 0.0,
    var deg: Int = 0,
    var gust: Double = 0.0
) : Parcelable

@Parcelize
open class Weather(
    @PrimaryKey(autoGenerate = false) var id: Int? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
) : Parcelable

@Parcelize
open class Sys(
    var type: Int = 0,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "sys_id") var id: Int? = null,
    var country: String? = null,
    var sunrise: Int = 0,
    var sunset: Int = 0
) : Parcelable

@Parcelize
open class Sys2(
    var pod: String? = null
) : Parcelable

@Parcelize
open class Main(
    var temp: Double = 0.0,
    var feels_like: Double = 0.0,
    var temp_min: Double = 0.0,
    var temp_max: Double = 0.0,
    var pressure: Int = 0,
    var humidity: Int = 0
) : Parcelable

@Parcelize
data class Main2(
    var temp: Double = 0.0,
    var feels_like: Double = 0.0,
    var temp_min: Double = 0.0,
    var temp_max: Double = 0.0,
    var pressure: Int = 0,
    var sea_level: Int = 0,
    var grnd_level: Int = 0,
    var humidity: Int = 0,
    var temp_kf: Double = 0.0
) : Parcelable

@Parcelize
@Entity(tableName = "current_weather_table")
open class WeatherResponse(
    @Embedded var coord: Coordinates? = null,
    var weather: List<Weather>? = null,
    var base: String? = null,
    @Embedded var main: Main? = null,
    var visibility: Int = 0,
    @Embedded var wind: Wind? = null,
    @Embedded var clouds: Clouds? = null,
    var dt: Int = 0,
    @Embedded(prefix = "client_bean_") var sys: Sys? = null,
    var timezone: Int,
    @PrimaryKey(autoGenerate = false) var id: Int? = null,
    var name: String? = null,
    var cod: Int = 0,
) : Parcelable

@Parcelize
open class ForecastResponse(
    var cod: Int = 0,
    var message: Int = 0,
    var cnt: Int = 0,
    var list: List<WeatherItem>? = null,
    var city: City? = null
) : Parcelable

@Parcelize
open class WeatherItem(
    var dt: Long = 0,
    var main: Main2? = null,
    var weather: List<Weather>? = null,
    var clouds: Clouds? = null,
    var wind: Wind? = null,
    var visibility: Int = 0,
    var pop: Double = 0.0,
    var sys: Sys2? = null,
    var dt_txt: String? = null
) : Parcelable

@Parcelize
open class Coordinates(
    var lon: Double = 0.0,
    var lat: Double = 0.0
) : Parcelable

@Parcelize
open class Clouds(
    var all: Int = 0
) : Parcelable

@Parcelize
open class City(
    var id: Int = 0,
    var name: String? = null,
    var coord: Coordinates? = null,
    var country: String? = null,
    var population: Int = 0,
    var timezone: Int = 0,
    var sunrise: Int = 0,
    var sunset: Int = 0
) : Parcelable

@Parcelize
data class ErrorResponse(var response: String? = null, var error: String? = null) : Parcelable







