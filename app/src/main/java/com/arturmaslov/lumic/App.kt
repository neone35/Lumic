package com.arturmaslov.lumic

import android.app.Application
import android.content.Context
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private lateinit var instance: App
        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }

}