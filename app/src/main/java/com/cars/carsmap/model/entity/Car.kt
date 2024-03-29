package com.cars.carsmap.model.entity

import com.google.android.gms.maps.model.LatLng

data class Car(
    val id:String,
    val latitude:Double = 0.0,
    val longitude:Double = 0.0,
    val modelIdentifier:String? = null,
    val modelName:String? = null,
    val name:String? = null,
    val make:String? = null,
    val group:String? = null,
    val color:String? = null,
    val series:String? = null,
    val fuelType:String? = null,
    val fuelLevel:Float? = null,
    val transmission:String? = null,
    val innerCleanliness:String? = null,
    val carImageUrl:String? = null) {

    override fun toString(): String = "[$id : $name]"

    val readableColor:String
    get() = color?.replace("_", " ")?.capitalize() ?: ""

    val latLng:LatLng
    get() = LatLng(latitude, longitude)
}