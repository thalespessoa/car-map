package com.cars.carsmap.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.viewmodel.CarsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment: SupportMapFragment(), OnMapReadyCallback {

    private val carsViewModel by lazy { activity?.let {
        ViewModelProviders.of(it, ViewModelFactory()).get(CarsViewModel::class.java) }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        carsViewModel?.viewState?.observe(this, Observer { viewState ->
            googleMap?.clear()
            viewState.list.forEach {
                val position = LatLng(it.latitude, it.longitude)
                googleMap?.addMarker(MarkerOptions().position(position).title(it.name))
            }
        })
    }
}