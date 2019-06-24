package com.cars.carsmap.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cars.carsmap.ApplicationComponent
import com.cars.carsmap.model.ApiResult
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.entity.Car
import kotlinx.coroutines.*
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * ViewModel responsible to ask data from 'DataRepository', build viewState object ('CarsViewState')
 * and provide this view states to the screens
 *
 * All actions result in a new CarsViewState
 *
 * @see DataRepository
 * @see CarsViewState
 */

class CarsViewModel : ViewModel(), ApplicationComponent.Injectable, CoroutineScope {

    @Inject
    lateinit var dataRepository: DataRepository

    private val supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisorJob

    val viewState: MutableLiveData<CarsViewState> by lazy {
        MutableLiveData<CarsViewState>().apply { value = CarsViewState(ViewStateStatus.SUCCESS) }
    }

    private val currentValue: CarsViewState?
        get() = viewState.value

    /**
     * Try to fetch cars list from server
     *
     * Steps:
     *
     * - Create a CarsViewState with status PROGRESS
     * - Call a method from 'DataRepository' responsible for fetch cars from server
     * - In case of success create a CarsViewState with status SUCCESS and the list of cars
     *   or in case of Fails create a CarsViewState with status ERROR and a error message
     *
     */
    fun refresh() = launch {
        viewState.value = CarsViewState(
            ViewStateStatus.PROGRESS,
            currentValue?.list ?: emptyList(),
            currentValue?.carSelected,
            null
        )

        dataRepository.fetchCars().let {
            when (it) {
                is ApiResult.Success -> viewState.value = CarsViewState(
                    ViewStateStatus.SUCCESS,
                    it.body,
                    currentValue?.carSelected,
                    null
                )
                is ApiResult.Error -> viewState.value = CarsViewState(
                    ViewStateStatus.ERROR,
                    currentValue?.list ?: emptyList(),
                    currentValue?.carSelected,
                    currentValue?.carSelectedMap,
                    message = it.exception.message
                )
            }
        }
    }

    /**
     * Select a car to show it's details in a dialog
     */
    fun select(car: Car) {
        currentValue
            ?.run { CarsViewState(status, list, car, message = message) }
            .also { viewState.value = it }
    }

    /**
     * Select a car to show it's location on the map
     */
    fun selectOnMap(car: Car) {
        currentValue
            ?.run { CarsViewState(status, list, carSelectedMap = car, message = message) }
            .also { viewState.value = it }
    }

    /**
     * This method should be called after user read the error message,
     * and it means the message will not be showed anymore until the next retry
     */
    fun readErrorMessage() {
        currentValue
            ?.run { CarsViewState(status, list, carSelected, carSelectedMap) }
            .also { viewState.value = it }
    }

    /**
     * Create a new view state (CarsViewState) with no 'carSelected'
     */
    fun unselect() {
        currentValue
            ?.run { CarsViewState(status, list, message = message) }
            .also { viewState.value = it }

    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }
}