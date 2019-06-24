package com.cars.carsmap

import com.cars.carsmap.model.ApiResult
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.NetworkApi
import com.cars.carsmap.model.entity.Car
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response


@RunWith(JUnit4::class)
class DataRepositoryTest {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var apiMock: NetworkApi.Api

    private val fakeList = listOf(Car("1"), Car("2"), Car("2"))
    private val completableDeferred = CompletableDeferred<Response<List<Car>>>()


    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        repository = DataRepository(apiMock, null)
    }

    @Test
    fun testFetchSuccess() = runBlocking {
        Mockito.`when`(apiMock.fetchCars()).thenReturn(completableDeferred)
        completableDeferred.complete(Response.success(fakeList))

        val result = repository.fetchCars()
        Assert.assertTrue(result is ApiResult.Success)
        Assert.assertEquals(fakeList, (result as ApiResult.Success).body)
    }

    @Test
    fun testFetchError() = runBlocking {
        Mockito.`when`(apiMock.fetchCars()).thenReturn(completableDeferred)
        completableDeferred.complete(Response.error(404, ResponseBody.create(null, "")))

        val result = repository.fetchCars()
        Assert.assertTrue(result is ApiResult.Error)
    }

}