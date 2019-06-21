package com.cars.carsmap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cars.carsmap.ApplicationComponent
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

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }

    fun refresh() = scope.launch {
        viewState.postValue(CarsViewState(ViewStateStatus.PROGRESS))
        kotlin.runCatching {
            dataRepository.fetchCars()
        }.onSuccess {
            viewState.postValue(CarsViewState(ViewStateStatus.SUCCESS, it))
        }.onFailure {
            viewState.postValue(CarsViewState(ViewStateStatus.ERROR, message = it.message))
        }
    }

    fun select(car: Car) {
        viewState.value
            ?.run { CarsViewState(status, list, car, carSelectedMap, message) }
            .also { viewState.value = it }
    }

    fun selectOnMap(car: Car) {
        viewState.value
            ?.run { CarsViewState(status, list, carSelected, car, message) }
            .also { viewState.value = it }
    }

    fun unselect() {
        viewState.value
            ?.run { CarsViewState(status, list, message = message) }
            .also { viewState.value = it }
    }
}