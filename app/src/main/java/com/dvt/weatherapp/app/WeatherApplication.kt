package com.dvt.weatherapp.app

import android.app.Application
import com.dvt.weatherapp.BuildConfig
import com.dvt.weatherapp.room.WeatherDatabase
import timber.log.Timber

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        WeatherDatabase.getInstance(this)
    }
}