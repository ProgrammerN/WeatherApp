package com.dvt.weatherapp.module

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesApplication(): Application {
        return Application()
    }
}