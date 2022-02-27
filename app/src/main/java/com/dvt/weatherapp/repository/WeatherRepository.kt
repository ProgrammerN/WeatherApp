package com.dvt.weatherapp.repository

import com.dvt.weatherapp.models.ForecastResponse
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.retrofit.RetrofitClient
import com.dvt.weatherapp.retrofit.WeatherAPIService
import retrofit2.Call

class WeatherRepository {

    private val weatherAPIService: WeatherAPIService by lazy {
        RetrofitClient.weatherAPIService
    }

    fun getSearchResponse(lat: Double, lon: Double): Call<WeatherResponse> {
        return weatherAPIService.currentWeather(lat, lon)
    }

    fun getWeatherForecastResponse(lat: Double, lon: Double): Call<ForecastResponse> {
        return weatherAPIService.weatherForecast(lat, lon)
    }
}