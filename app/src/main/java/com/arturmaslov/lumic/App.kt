package com.arturmaslov.lumic

import android.app.Application
import android.content.Context
import com.arturmaslov.lumic.di.cacheModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            // Load modules
            modules(
                listOf(
                    cacheModule
                )
            )
        }
    }

    companion object {
        private lateinit var instance: App
        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }

}