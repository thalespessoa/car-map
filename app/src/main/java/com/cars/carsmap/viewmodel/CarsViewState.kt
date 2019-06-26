package com.cars.carsmap.viewmodel

import android.view.View
import com.cars.carsmap.model.entity.Car

/**
 * It represents all possible states of the screens
 * It is instantiated by the ViewModel and listened by screens
 *
 * @param status screen status (Loading, Success or Error)
 * @param list list of cars available, can be displayed on a map or on a list.
 * @param carSelected car selected by user, to see the details in a dialog
 * @param carSelectedMap car selected by user, to see it's location on map
 * @param message error message in case of fail while fetching list of cars
 *
 * @see CarsViewModel
 *
 */
data class CarsViewState(
    val status: ViewStateStatus,
    val list: List<Car> = emptyList(),
    val carSelected: Car? = null,
    val carSelectedMap: Car? = null,
    val message: String? = null
) {

    val isLoading: Boolean
        get() {
            return status == ViewStateStatus.PROGRESS
        }

    val emptyVisibility:Int
        get() {
            return if(!isLoading && list.isEmpty())
                View.VISIBLE
            else
                View.INVISIBLE
        }
}