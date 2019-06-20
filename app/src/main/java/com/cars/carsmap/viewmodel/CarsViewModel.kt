package com.cars.carsmap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cars.carsmap.ApplicationComponent
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.entity.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class CarsViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    private val supervisorJob = SupervisorJob()
    private val defaultScope = CoroutineScope(Dispatchers.Default + supervisorJob)

    val viewState: MutableLiveData<CarsViewState> by lazy {
        MutableLiveData<CarsViewState>()
            .apply { value = CarsViewState(ViewStateStatus.SUCCESS) }
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancel()
    }

    fun refresh() = defaultScope.launch {
        viewState.postValue(CarsViewState(ViewStateStatus.RUNNING))
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
            ?.run { CarsViewState(status, list, car, message) }
            .also { viewState.value = it }
    }

    fun unselect() {
        viewState.value
            ?.run { CarsViewState(status, list, message = message) }
            .also { viewState.value = it }
    }

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }
}