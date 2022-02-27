package com.dvt.weatherapp.component

import com.dvt.weatherapp.module.AppModule
import com.dvt.weatherapp.viewmodels.LocalWeatherViewModel
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun localWeatherViewModel(): LocalWeatherViewModel

}
