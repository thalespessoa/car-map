package com.cars.carsmap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cars.carsmap.ApplicationComponent
import com.cars.carsmap.model.ApiResult
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.entity.Car
import kotlinx.coroutines.*
import javax.inject.Inject

class CarsViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + supervisorJob)

    val viewState: MutableLiveData<CarsViewState> by lazy {
        MutableLiveData<CarsViewState>().apply { value = CarsViewState(ViewStateStatus.SUCCESS) }
    }

    private val currentValue: CarsViewState?
        get() = viewState.value

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }

    fun refresh() = scope.launch {
        viewState.postValue(
            CarsViewState(
                ViewStateStatus.PROGRESS,
                currentValue?.list ?: emptyList(),
                currentValue?.carSelected,
                null
            )
        )

        dataRepository.fetchCars().let {
            when (it) {
                is ApiResult.Success -> viewState.postValue(
                    CarsViewState(
                        ViewStateStatus.SUCCESS,
                        it.body,
                        currentValue?.carSelected,
                        null
                    )
                )
                is ApiResult.Error -> viewState.postValue(
                    CarsViewState(
                        ViewStateStatus.ERROR,
                        currentValue?.list ?: emptyList(),
                        currentValue?.carSelected,
                        currentValue?.carSelectedMap,
                        message = it.exception.message
                    )
                )
            }
        }
    }

    fun select(car: Car) {
        currentValue
            ?.run { CarsViewState(status, list, car, message = message) }
            .also { viewState.value = it }
    }

    fun selectOnMap(car: Car) {
        currentValue
            ?.run { CarsViewState(status, list, carSelectedMap = car, message = message) }
            .also { viewState.value = it }
    }

    fun readErrorMessage() {
        currentValue
            ?.run { CarsViewState(status, list, carSelected, carSelectedMap) }
            .also { viewState.value = it }
    }

    fun unselect() {
        currentValue
            ?.run { CarsViewState(status, list, message = message) }
            .also { viewState.value = it }

    }
}