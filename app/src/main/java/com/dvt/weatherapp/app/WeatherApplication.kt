package com.dvt.weatherapp.app

import android.app.Application
import com.dvt.weatherapp.room.WeatherDatabase

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WeatherDatabase.getInstance(this)

    }
}