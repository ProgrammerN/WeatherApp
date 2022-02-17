package com.dvt.weatherapp.activities.home.ui.home

import androidx.test.core.app.ApplicationProvider
import com.dvt.weatherapp.viewmodels.LocalWeatherViewModel
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class HomeFragmentTest{

    @Test
    fun favoriteWeather_tasksDisplayFavoriteState() {
        // Given a fresh ViewModel
        val tasksViewModel = LocalWeatherViewModel(ApplicationProvider.getApplicationContext())

        //CHeck weather no existing id returns false
        assertThat(tasksViewModel.exists(0), `is`(false))
    }
}