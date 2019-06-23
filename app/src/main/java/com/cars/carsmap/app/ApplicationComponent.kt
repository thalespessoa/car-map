package com.cars.carsmap

import com.cars.carsmap.viewmodel.CarsViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Dagger component to handle dependency injection
 */

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(viewModel: CarsViewModel)

    interface Injectable {
        fun inject(applicationComponent: ApplicationComponent)
    }
}