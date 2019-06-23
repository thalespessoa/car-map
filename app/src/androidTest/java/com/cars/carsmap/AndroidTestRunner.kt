package com.cars.carsmap

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.github.tmurakami.dexopener.DexOpener

class AndroidTestRunner :AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        DexOpener.install(this)
        return super.newApplication(cl, AndroidTestApplication::class.java.name, context)
    }

    override fun callApplicationOnCreate(app: Application) {
        super.callApplicationOnCreate(app)
    }
}