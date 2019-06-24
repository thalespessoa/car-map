package com.cars.carsmap.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.view.adapter.MapInfoWindowAdapter
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Fragment responsible to show the cars list (CarsViewState.list) on the map
 */
class MapFragment : SupportMapFragment(),
    OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener,
    Observer<CarsViewState> {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var map: GoogleMap? = null
    private var currentLatLng: LatLng? = null

    private val carsViewModel by lazy {
        activity?.let { ViewModelProviders.of(it, ViewModelFactory()).get(CarsViewModel::class.java) }
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
        googleMap?.let { setUpMap(it) }
        carsViewModel?.viewState?.observe(this, this)
    }

    override fun onChanged(viewState: CarsViewState?) {
        map?.clear()

        if(currentLatLng == null)
            viewState?.list?.firstOrNull()?.latLng?.let { position ->
                currentLatLng = position
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 8f))
            }

        viewState?.list?.forEach { car ->
            MarkerOptions()
                .position(car.latLng)
                .title(car.modelName)
                .let { map?.addMarker(it) }
                .also {
                    it?.tag = car
                    if (car == viewState.carSelectedMap) {
                        it?.showInfoWindow()
                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(it?.position, 12f))
                    }
                }
        }
    }

    private fun setUpMap(googleMap: GoogleMap) {
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setInfoWindowAdapter(MapInfoWindowAdapter(layoutInflater))

        activity?.let {
            if (ActivityCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(it, arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            else
                requestLocalization()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE)
            requestLocalization()
    }

    override fun onInfoWindowClick(marker: Marker?) {
        marker?.tag?.let {
            if (it is Car) carsViewModel?.select(it)
        }
    }

    private fun requestLocalization() {
        activity?.let {
            if (ActivityCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                map?.isMyLocationEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener(it) { location ->
                    this.currentLatLng = LatLng(location.latitude, location.longitude)
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }
}