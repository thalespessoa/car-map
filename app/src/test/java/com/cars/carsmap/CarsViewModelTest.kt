package com.cars.carsmap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.ViewStateStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class CarsViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val viewModel: CarsViewModel = CarsViewModel()

    @Test
    fun testSelect() = runBlocking {

        viewModel.select(Car("2"))

        Assert.assertEquals(ViewStateStatus.SUCCESS, viewModel.viewState.value?.status)
        Assert.assertNotNull(viewModel.viewState.value?.carSelected)
        Assert.assertNull(viewModel.viewState.value?.carSelectedMap)
        Assert.assertNull(viewModel.viewState.value?.message)
        Assert.assertEquals(arrayListOf<Car>(), viewModel.viewState.value?.list)
    }

    @Test
    fun testUnselect() = runBlocking {

        viewModel.select(Car("2"))
        viewModel.unselect()

        Assert.assertEquals(ViewStateStatus.SUCCESS, viewModel.viewState.value?.status)
        Assert.assertNull(viewModel.viewState.value?.carSelected)
        Assert.assertNull(viewModel.viewState.value?.carSelectedMap)
        Assert.assertNull(viewModel.viewState.value?.message)
        Assert.assertEquals(arrayListOf<Car>(), viewModel.viewState.value?.list)
    }

    @Test
    fun testSelectMap() = runBlocking {

        viewModel.selectOnMap(Car("2"))

        Assert.assertEquals(ViewStateStatus.SUCCESS, viewModel.viewState.value?.status)
        Assert.assertNull(viewModel.viewState.value?.carSelected)
        Assert.assertNotNull(viewModel.viewState.value?.carSelectedMap)
        Assert.assertNull(viewModel.viewState.value?.message)
        Assert.assertEquals(arrayListOf<Car>(), viewModel.viewState.value?.list)
    }
}