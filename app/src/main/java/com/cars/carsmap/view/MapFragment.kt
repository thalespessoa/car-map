package com.cars.carsmap.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.viewmodel.CarsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var map: GoogleMap? = null

    private val carsViewModel by lazy {
        activity?.let {
            ViewModelProviders.of(it, ViewModelFactory()).get(CarsViewModel::class.java)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        getMapAsync(this)
    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        setUpMap()
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        carsViewModel?.viewState?.observe(this, Observer { viewState ->
            googleMap?.clear()
            viewState.list.forEach { car ->
                MarkerOptions()
                    .position(LatLng(car.latitude, car.longitude))
                    .title(car.name)
                    .let { googleMap?.addMarker(it) }
                    .let { it?.tag = car }
            }
        })
    }

    private fun setUpMap() {
        activity?.let { activity ->
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
        map?.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(activity!!) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
//                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.tag?.let { tag ->
            if(tag is Car) carsViewModel?.select(tag)
        }

        return true
    }
}