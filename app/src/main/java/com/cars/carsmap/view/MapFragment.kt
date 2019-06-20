package com.cars.carsmap.view

import android.os.Bundle
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
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        googleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}