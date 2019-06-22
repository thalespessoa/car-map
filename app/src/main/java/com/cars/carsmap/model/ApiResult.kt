package com.cars.carsmap.model


sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val body: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()
}