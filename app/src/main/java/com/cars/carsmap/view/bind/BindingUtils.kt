package com.cars.carsmap.view.bind

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

@BindingAdapter("data")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as BindableAdapter<T>).setData(items)
    }
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}