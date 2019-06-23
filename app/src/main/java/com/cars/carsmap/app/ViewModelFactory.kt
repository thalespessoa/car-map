package com.cars.carsmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cars.carsmap.app.ApplicationController

open class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val t = super.create(modelClass)
        if (t is ApplicationComponent.Injectable) {
            (t as ApplicationComponent.Injectable).inject(ApplicationController.graph)
        }
        return t
    }
}