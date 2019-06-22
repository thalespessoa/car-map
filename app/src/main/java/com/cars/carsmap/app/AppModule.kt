package com.cars.carsmap

import android.app.Application
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.NetworkApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application:Application) {
    @Provides
    @Singleton
    fun getDataRepository() = DataRepository(NetworkApi().api, application)
}