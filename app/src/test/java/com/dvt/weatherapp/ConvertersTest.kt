package com.dvt.weatherapp

import com.dvt.weatherapp.room.Converters
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConvertersTest {

    @Test
    fun `test convert string value to double`() {
        val stringValue = "1.98"
        val converters = Converters()
        val result = converters.toDouble(stringValue)
        assertThat(result).isAtMost(1.98)
        assertThat(result).isAtLeast(1.98)
    }

    @Test
    fun `test convert double value to string`() {
        val doubleValue = 1.98
        val converters = Converters()
        val result = converters.doubleToString(doubleValue)
        assertThat(result).isEqualTo("1.98")
    }
}