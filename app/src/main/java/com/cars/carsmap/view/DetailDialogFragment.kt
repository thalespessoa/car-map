package com.cars.carsmap.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.cars.carsmap.DetailBinding
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import kotlinx.android.synthetic.main.fragment_car_detail.*

/**
 * Dialog responsible to show cars details (CarsViewState.carSelected)
 *
 * All possible states are represented by a unique and immutable CarsViewState inside the CarsViewModel
 *
 * @see CarsViewState
 * @see CarsViewModel
 */
class DetailDialogFragment: DialogFragment() {

    @VisibleForTesting
    var viewModelFactory = ViewModelFactory()

    private val carsViewModel by lazy {
        activity?.let { ViewModelProviders.of(it, viewModelFactory).get(CarsViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<DetailBinding>(inflater, R.layout.fragment_car_detail, container, false)
            .apply {
                lifecycleOwner = this@DetailDialogFragment
                viewModel = carsViewModel
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeButton.setOnClickListener {
            carsViewModel?.unselect()
            dismiss()
        }

        locationButton.setOnClickListener {
            carsViewModel?.viewState?.value?.carSelected?.let {car ->
                carsViewModel?.unselect()
                carsViewModel?.selectOnMap(car)
                dismiss()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        carsViewModel?.unselect()
    }
}