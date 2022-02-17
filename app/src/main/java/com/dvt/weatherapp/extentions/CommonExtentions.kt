package com.dvt.weatherapp.extentions

import android.content.Context
import android.widget.Toast
import com.dvt.weatherapp.models.ErrorResponse
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException

fun Context.showErrorMessage(errorBody: ResponseBody, duration: Int = Toast.LENGTH_SHORT) {
    val gson = GsonBuilder().create()
    try {
        val errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
        toast(errorResponse.error!!, duration)

    } catch (e: IOException) {
        Timber.i(e.toString())
    }
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}