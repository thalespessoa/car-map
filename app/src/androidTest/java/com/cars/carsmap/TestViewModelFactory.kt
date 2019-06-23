package com.cars.carsmap

import androidx.lifecycle.ViewModel


class TestViewModelFactory<T>(private val viewModel: T) : ViewModelFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
}