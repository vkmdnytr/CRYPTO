package com.bitcoin.ticker

import android.app.Application
import android.content.Context
import androidx.multidex.BuildConfig
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BitCoinApplication  : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
        if (BuildConfig.DEBUG) {

        }
    }

}