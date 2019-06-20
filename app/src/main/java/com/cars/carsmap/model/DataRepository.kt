package com.cars.carsmap.model

/**
 * Created by thalespessoa on 19/5/18.
 */
class DataRepository(private val networkApi: NetworkApi) {

    suspend fun fetchCars() = networkApi.fetchCars().await()
}