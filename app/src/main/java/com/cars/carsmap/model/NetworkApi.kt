package com.cars.carsmap.model

import com.cars.carsmap.model.entity.Car
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * Class responsible for the communication with server
 * All remote data access comes from here.
 */

class NetworkApi {

    companion object {
        private const val BASE_URL = "https://cdn.sixt.io/"
        private const val LIST_URL = "/codingtask/cars"
    }

    private val clientBuilder = OkHttpClient.Builder().build()

    val api: Api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(clientBuilder)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(Api::class.java)

    //----------------------------------------------------------------------------------------------
    // API
    //----------------------------------------------------------------------------------------------

    interface Api {
        @GET(LIST_URL)
        fun fetchCars(): Deferred<Response<List<Car>>>
    }
}