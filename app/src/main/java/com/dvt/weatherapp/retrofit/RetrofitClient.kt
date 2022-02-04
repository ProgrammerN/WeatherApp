package com.dvt.weatherapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherAPIService: WeatherAPIService = retrofit.create(WeatherAPIService::class.java)
}