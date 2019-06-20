package com.cars.carsmap

import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.NetworkApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun getNetworkApi(): NetworkApi = NetworkApi()

    @Provides
    @Singleton
    fun getDataRepository(networkApi: NetworkApi) = DataRepository(networkApi)
}