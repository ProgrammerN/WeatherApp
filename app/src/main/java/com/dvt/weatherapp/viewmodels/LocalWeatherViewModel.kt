package com.dvt.weatherapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.repository.LocalWeatherRepository

class LocalWeatherViewModel(application: Application) : AndroidViewModel(application) {

    private var saveWeatherResponse: LiveData<List<WeatherResponse>>? = null
    private var exist: LiveData<Boolean>? = null
    private val repository = LocalWeatherRepository(application)


    fun insert(superhero: WeatherResponse) {
        repository.insert(superhero)
    }

    fun update(weatherResponse: WeatherResponse) {
        repository.update(weatherResponse)
    }

    fun delete(weatherResponse: WeatherResponse) {
        repository.delete(weatherResponse)
    }

    fun exists(id: Int): LiveData<Boolean>? {
        exist = repository.exists(id)
        return exist
    }


    fun getAllCurrentWeather(): LiveData<List<WeatherResponse>>? {
        saveWeatherResponse = repository.getAllCurrentWeather()
        return saveWeatherResponse
    }
}