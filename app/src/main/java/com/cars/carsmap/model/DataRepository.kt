package com.cars.carsmap.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DataRepository(private val api: NetworkApi) {

    suspend fun fetchCars() = withContext(Dispatchers.IO) { api.fetchCars().await() }
}