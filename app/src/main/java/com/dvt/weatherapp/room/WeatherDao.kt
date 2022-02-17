package com.dvt.weatherapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.dvt.weatherapp.models.WeatherResponse

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertCurrentWeather(weatherResponse: WeatherResponse)

    @Update
    suspend fun update(weatherResponse: WeatherResponse)

    @Delete
    suspend fun delete(weatherResponse: WeatherResponse)

    @Query("SELECT EXISTS(SELECT * FROM current_weather_table WHERE id=(:id))")
    fun exists(id: Int): LiveData<Boolean>

    /*@Query("delete from superheroes_table")
    suspend fun deleteAllSuperheros()*/

    @Query("select * from current_weather_table")
    fun getAllCurrentWeather(): LiveData<List<WeatherResponse>>
}