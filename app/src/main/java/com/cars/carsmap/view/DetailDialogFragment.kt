package com.cars.carsmap.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_car_detail.*

/**
 * Dialog responsible to show cars details (CarsViewState.carSelected)
 *
 * All possible states are represented by a unique and immutable CarsViewState inside the CarsViewModel
 *
 * @see CarsViewState
 * @see CarsViewModel
 */
class DetailDialogFragment: DialogFragment(), Observer<CarsViewState> {

    @VisibleForTesting
    var viewModelFactory = ViewModelFactory()

    private val carsViewModel by lazy {
        activity?.let { ViewModelProviders.of(it, viewModelFactory).get(CarsViewModel::class.java) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carsViewModel?.viewState?.observe(this, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_car_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeButton.setOnClickListener {
            carsViewModel?.unselect()
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        carsViewModel?.unselect()
    }

    override fun onChanged(viewState: CarsViewState?) {
        viewState?.carSelected?.also { car ->
            title.text = car.modelName
            color.text = car.readableColor
            name.text = car.name
            group.text = car.group
            serie.text = car.series
            Picasso.with(context).load(car.carImageUrl).error(R.drawable.baseline_directions_car_24px).into(image)

            locationButton.setOnClickListener {
                carsViewModel?.unselect()
                carsViewModel?.selectOnMap(car)
                dismiss()
            }
        }
    }
}