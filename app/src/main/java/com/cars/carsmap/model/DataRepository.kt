package com.cars.carsmap.model

import android.app.Application
import com.cars.carsmap.R
import com.cars.carsmap.model.NetworkApi.Api
import com.cars.carsmap.model.entity.Car
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException


class DataRepository(private val api: Api, private var application: Application?) {

    //----------------------------------------------------------------------------------------------
    // Public
    //----------------------------------------------------------------------------------------------

    suspend fun fetchCars(): ApiResult<List<Car>> = doSafeRequest { api.fetchCars() }

    //----------------------------------------------------------------------------------------------
    // Private
    //----------------------------------------------------------------------------------------------

    private suspend fun <T : Any> doSafeRequest(call: suspend () -> Deferred<Response<T>>): ApiResult<T> =
        try {
            withContext(Dispatchers.IO) { call.invoke().await() }
                .let {
                    when {
                        it.isSuccessful -> ApiResult.Success(it.body()!!)
                        else -> handleFailResponse(it)
                    }
                }
        } catch (exception: Exception) {
            handleFailException(exception)
        }

    private fun <T>handleFailResponse(response:Response<T>) =
        when {
            response.message().isNotEmpty() -> response.message()
            else -> application?.resources?.getString(R.string.error_server)
        }.let {
            ApiResult.Error(Exception(it))
        }

    private fun handleFailException(exception:Exception) =
        when(exception) {
            is UnknownHostException -> application?.resources?.getString(R.string.error_no_internet)
            is IOException -> application?.resources?.getString(R.string.error_server)
            else -> application?.resources?.getString(R.string.error_unknown)
        }.let {
            ApiResult.Error(Exception(it))
        }

}