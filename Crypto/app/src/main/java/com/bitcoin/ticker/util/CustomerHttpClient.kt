package com.bitcoin.ticker.util

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class CustomHttpLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "CustomHttpLogger"
        if (message.startsWith("{") || message.startsWith("[")) {
            Log.d("CustomHttpLogger", message)
        } else {
            Log.d(logName, message)
            return
        }
    }
}