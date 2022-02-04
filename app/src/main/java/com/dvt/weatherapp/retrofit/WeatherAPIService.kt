package com.dvt.weatherapp.retrofit

import com.dvt.weatherapp.app.Constants.API_KEY
import com.dvt.weatherapp.app.Constants.TEMP_UNITS
import com.dvt.weatherapp.models.ForecastResponse
import com.dvt.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {

    @GET("weather")
    fun currentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = API_KEY,
        @Query("units") units: String = TEMP_UNITS
    ): Call<WeatherResponse>

    @GET("forecast")
    fun weatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = API_KEY,
        @Query("units") units: String = TEMP_UNITS
    ): Call<ForecastResponse>

}