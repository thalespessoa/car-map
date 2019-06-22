package com.cars.carsmap

import com.cars.carsmap.model.ApiResult
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.NetworkApi
import com.cars.carsmap.model.entity.Car
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response


@RunWith(JUnit4::class)
class DataRepositoryTest {

    private val fakeList = listOf(Car("1"), Car("2"), Car("2"))
    private val completableDeferred = CompletableDeferred<Response<List<Car>>>()

    @MockK
    lateinit var apiMock: NetworkApi.Api

    private var repository: DataRepository

    init {
        MockKAnnotations.init(this)
        repository = DataRepository(apiMock, null)
    }

    @Before
    fun setUp() {

    }

    @Test
    fun testFetchSuccess() = runBlocking {
        coEvery { apiMock.fetchCars() } returns completableDeferred
        completableDeferred.complete(Response.success(fakeList))

        val result = repository.fetchCars()
        Assert.assertTrue(result is ApiResult.Success)
        Assert.assertEquals(fakeList, (result as ApiResult.Success).body)
    }

    @Test
    fun testFetchError() = runBlocking {
        coEvery { apiMock.fetchCars() } returns completableDeferred
        completableDeferred.complete(Response.error(404, ResponseBody.create(null, "")))

        val result = repository.fetchCars()
        Assert.assertTrue(result is ApiResult.Error)
    }

}