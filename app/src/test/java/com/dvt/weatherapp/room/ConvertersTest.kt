package com.dvt.weatherapp.room

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase
import org.junit.Test

class ConvertersTest {

    @Test
    fun test_StringValue_To_Double() {
        val stringValue = "1.98"
        val converters = Converters()
        val result = converters.toDouble(stringValue)
        assertThat(result, `is`(1.98))
    }

    @Test
    fun test_DoubleValue_To_String() {
        val doubleValue = 1.98
        val converters = Converters()
        val result = converters.doubleToString(doubleValue)
        assertThat(result, equalToIgnoringCase("1.98"))
    }
}