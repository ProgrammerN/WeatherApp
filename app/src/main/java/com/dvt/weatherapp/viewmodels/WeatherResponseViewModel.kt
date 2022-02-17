package com.dvt.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvt.weatherapp.models.ForecastResponse
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.repository.WeatherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherResponseViewModel : ViewModel() {

    private var weatherRepository: WeatherRepository? = null
    val errorMessage = MutableLiveData<String>()

    init {
        weatherRepository = WeatherRepository()
    }

    private val currentWeatherResponse = MutableLiveData<Response<WeatherResponse>>()
    private val weatherForecastResponse = MutableLiveData<Response<ForecastResponse>>()

    fun getWeatherResponse(lat: Double, lon: Double): LiveData<Response<WeatherResponse>> {
        weatherRepository?.getSearchResponse(lat, lon)?.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                currentWeatherResponse.value = response
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                errorMessage.value = t.toString()
            }
        })

        return currentWeatherResponse
    }

    fun getWeatherForecastResponse(lat: Double, lon: Double): LiveData<Response<ForecastResponse>> {
        weatherRepository?.getWeatherForecastResponse(lat, lon)
            ?.enqueue(object : Callback<ForecastResponse> {
                override fun onResponse(
                    call: Call<ForecastResponse>,
                    response: Response<ForecastResponse>
                ) {
                    weatherForecastResponse.value = response
                }

                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    errorMessage.value = t.toString()
                }
            })

        return weatherForecastResponse
    }
}