package com.cars.carsmap.view.adapter

import android.view.LayoutInflater
import android.view.View
import com.cars.carsmap.R
import com.cars.carsmap.model.entity.Car
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.item_info_window_car.view.*


class MapInfoWindowAdapter(private val inflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker?): View? {
        val car = marker?.tag as? Car
        return car?.run {
            val view = inflater.inflate(R.layout.item_info_window_car, null)
            view.modelName.text = modelName
            view.title.text = name
            view
        }
    }

    override fun getInfoWindow(marker: Marker?): View? = null
}