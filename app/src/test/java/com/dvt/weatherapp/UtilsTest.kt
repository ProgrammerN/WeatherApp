package com.dvt.weatherapp

import com.dvt.weatherapp.util.Utils
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun `test if correct day of the week from timestamp`() {
        val timestamp: Long = 1645364491 //Sunday, February 20, 2022 3:41:31 PM GMT+02:00
        val result = Utils.getDayOfTheWeek(timestamp)
        assertThat(result).isEqualTo("Sunday")
    }

    @Test
    fun `test if wrong day of the week from timestamp`() {
        val timestamp: Long = 1645364491 //Sunday, February 20, 2022 3:41:31 PM GMT+02:00
        val result = Utils.getDayOfTheWeek(timestamp)
        assertThat(result).isNotEqualTo("Friday")
    }

    @Test
    fun `test if date is understandable from time stamp`() {
        val timestamp: Long = 1645364491 //3:41 Sunday using format HH:mm EEEE
        val result = Utils.getUnderstandableDate(timestamp)
        assertThat(result).isEqualTo("15:41 Sunday")
    }

    @Test
    fun `test if date is understandable but wrong`() {
        val timestamp: Long = 1645364491 //3:41 Sunday using format HH:mm EEEE
        val result = Utils.getUnderstandableDate(timestamp)
        assertThat(result).isNotEqualTo("15:45 Friday")
    }

    @Test
    fun `test if temperature is in readable format`() {
        val temp = 4.35
        val result = Utils.formatTemperature(temp)
        assertThat(result).isEqualTo("4.4°")
    }

    @Test
    fun `test if temperature is in readable format and wrong`() {
        val temp = 4.35
        val result = Utils.formatTemperature(temp)
        assertThat(result).isNotEqualTo("4.5°")
    }
}