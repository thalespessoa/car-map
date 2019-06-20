package com.cars.carsmap.viewmodel

import com.cars.carsmap.model.entity.Car

data class CarsViewState(
    val status: ViewStateStatus,
    val list: List<Car> = emptyList(),
    val carSelected: Car? = null,
    val message:String? = null)