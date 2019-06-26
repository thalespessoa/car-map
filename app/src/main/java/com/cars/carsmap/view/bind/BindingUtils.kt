package com.cars.carsmap.view.bind

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cars.carsmap.R
import com.squareup.picasso.Picasso

@BindingAdapter("data")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as BindableAdapter<T>).setData(items)
    }
}

@BindingAdapter("imageUrl")
fun <T> setImageViewProperties(imageView: ImageView, imageUrl:String) {
    Picasso.with(imageView.context)
        .load(imageUrl)
        .error(R.drawable.baseline_directions_car_24px)
        .into(imageView)
}