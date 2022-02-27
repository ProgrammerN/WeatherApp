package com.dvt.weatherapp.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dvt.weatherapp.models.*
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest : TestCase() {

    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        weatherDatabase = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java).build()
        dao = weatherDatabase.WeatherDao();

    }

    @After
    public override fun tearDown() {
        weatherDatabase.close()
    }

    @Test
    fun writeAndReadWeatherResponse() = runBlocking {

        val coord = Coordinates(139.0, 35.0)
        val weather = Weather(800, "Cloudy", "clear sky", "10n")
        val main = Main(0.0, 0.0, 0.0, 0.0, 0, 0)
        val wind = Wind(0.0, 0, 0.0)
        val clouds = Clouds(0)
        val sys = Sys(3, 1234, "South Africa", 0, 0)

        val weatherResponse = WeatherResponse(
            coord, listOf(weather), "Some Base",
            main,
            868686,
            wind,
            clouds,
            1764673774,
            sys,
            8588686,
            1234,
            "Sandton",
            8968886
        )

        dao.insertCurrentWeather(weatherResponse)

        val allCurrentWeather = dao.getAllCurrentWeather()

        assertThat(allCurrentWeather.value?.contains(weatherResponse)).isTrue()
    }
}