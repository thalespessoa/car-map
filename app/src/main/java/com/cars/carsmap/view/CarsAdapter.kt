package com.cars.carsmap.view

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cars.carsmap.R
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.view.bind.BindableAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_car.view.*

class CarsAdapter(private val onSelectCar: (Car) -> Unit) : RecyclerView.Adapter<CarsAdapter.Holder>(), BindableAdapter<Car> {

    private var items = emptyList<Car>()

    override fun setData(items: List<Car>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(
            inflater.inflate(R.layout.item_car, parent, false),
            onSelectCar
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    //----------------------------------------------------------------------------------------------
    // View Holder
    //----------------------------------------------------------------------------------------------

    class Holder(itemView: View, private val onSelect: (Car) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(car: Car) {
            itemView.title.text = car.name
            Picasso.with(itemView.context).load(car.carImageUrl).into(itemView.image)
            itemView.setOnClickListener { onSelect(car) }
        }
    }
}