package com.cars.carsmap.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cars.carsmap.R
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.view.bind.BindableAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_car.view.*

class CarsListAdapter : RecyclerView.Adapter<CarsListAdapter.Holder>(), BindableAdapter<Car> {

    private var items = emptyList<Car>()

    var onCarSelected: ((Car) -> Unit)? = null
    var onCarPlaceSelected: ((Car) -> Unit)? = null

    override fun setData(items: List<Car>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        LayoutInflater.from(parent.context).let {
            Holder(it.inflate(R.layout.item_car, parent, false))
        }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

    //----------------------------------------------------------------------------------------------
    // View Holder
    //----------------------------------------------------------------------------------------------

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(car: Car) {
            itemView.title.text = car.name
            itemView.modelName.text = car.modelName
            Picasso.with(itemView.context).load(car.carImageUrl).into(itemView.image)
            itemView.setOnClickListener { onCarSelected?.invoke(car) }
            itemView.placeButton.setOnClickListener { onCarPlaceSelected?.invoke(car) }
        }
    }
}