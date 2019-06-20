package com.cars.carsmap.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.cars.carsmap.ApplicationComponent
import com.cars.carsmap.model.DataRepository
import com.cars.carsmap.model.entity.Car
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CarsViewModel : ViewModel(), ApplicationComponent.Injectable {

    @Inject
    lateinit var dataRepository: DataRepository

    val viewState: MutableLiveData<CarsViewState> by lazy {
        MutableLiveData<CarsViewState>()
            .apply { value = CarsViewState(ViewStateStatus.SUCCESS) }
    }

    fun refresh() = GlobalScope.launch {
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
        println("Select: $car")
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