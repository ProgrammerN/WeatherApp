package com.dvt.weatherapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.room.WeatherDao
import com.dvt.weatherapp.room.WeatherDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LocalWeatherRepository(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main
    private var weatherDao: WeatherDao?
    private var weatherResponseList: LiveData<List<WeatherResponse>>? = null

    init {
        val db = WeatherDatabase.getInstance(application)
        weatherDao = db.WeatherDao()
    }

    fun insert(superhero: WeatherResponse) {
        launch {
            insertWeatherResponse(superhero)
        }
    }

    fun update(superhero: WeatherResponse) {
        launch {
            updateWeatherResponse(superhero)
        }
    }

    fun delete(superhero: WeatherResponse) {
        launch {
            deleteWeatherResponse(superhero)
        }
    }

    fun exists(id: Int): LiveData<Boolean>? {
        return weatherDao?.exists(id)
    }


    private suspend fun insertWeatherResponse(weatherResponse: WeatherResponse) {
        withContext(Dispatchers.IO) {
            weatherDao?.insertCurrentWeather(weatherResponse)
        }
    }

    private suspend fun updateWeatherResponse(weatherResponse: WeatherResponse) {
        withContext(Dispatchers.IO) {
            weatherDao?.update(weatherResponse)
        }
    }

    private suspend fun deleteWeatherResponse(weatherResponse: WeatherResponse) {
        withContext(Dispatchers.IO) {
            weatherDao?.delete(weatherResponse)
        }
    }

    fun getAllCurrentWeather(): LiveData<List<WeatherResponse>>? {
        weatherResponseList = weatherDao?.getAllCurrentWeather()
        return weatherResponseList
    }
}