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

    suspend fun fetchCars(): ApiResult<List<Car>> = doSafeRequest { api.fetchCars() }


    private suspend fun <T : Any> doSafeRequest(call: suspend () -> Deferred<Response<T>>): ApiResult<T> =
        try {
            withContext(Dispatchers.IO) { call.invoke().await() }
                .let {
                    when {
                        it.isSuccessful -> ApiResult.Success(it.body()!!)
                        it.message().isNotEmpty() -> ApiResult.Error(java.lang.Exception(it.message()))
                        else -> ApiResult.Error(java.lang.Exception(application?.resources?.getString(R.string.error_server)))
                    }

                }
        } catch (exception: UnknownHostException) {
            ApiResult.Error(java.lang.Exception(application?.resources?.getString(R.string.error_no_internet)))
        } catch (exception: IOException) {
            ApiResult.Error(java.lang.Exception(application?.resources?.getString(R.string.error_server)))
        } catch (exception: Exception) {
            ApiResult.Error(java.lang.Exception(application?.resources?.getString(R.string.error_unknown)))
        }
}