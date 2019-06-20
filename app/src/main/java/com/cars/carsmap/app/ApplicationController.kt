package com.cars.carsmap.app

import android.app.Application
import com.cars.carsmap.AppModule
import com.cars.carsmap.ApplicationComponent
import com.cars.carsmap.BuildConfig
import com.cars.carsmap.DaggerApplicationComponent
import com.facebook.stetho.Stetho

class ApplicationController : Application() {

    companion object {
        @JvmStatic
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().appModule(AppModule()).build()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}