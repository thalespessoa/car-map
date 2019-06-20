package com.cars.carsmap.view

import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cars.carsmap.R
import com.cars.carsmap.ListBinding
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.viewmodel.CarsViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { swipe_refresh_layout }
    private val recyclerView: RecyclerView by lazy { recycler_view }
    private val carsAdapter = CarsAdapter { onSelect(it) }

    private val carsViewModel by lazy { activity?.let {
        ViewModelProviders.of(it, ViewModelFactory()).get(CarsViewModel::class.java) }
    }

    //----------------------------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<ListBinding>(inflater, R.layout.fragment_list, container, false)
            .apply {
                setLifecycleOwner(this@ListFragment)
                viewModel = carsViewModel
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = carsAdapter
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    //----------------------------------------------------------------------------------------------
    // Actions
    //----------------------------------------------------------------------------------------------

    override fun onRefresh() {
        carsViewModel?.refresh()
    }

    private fun onSelect(car:Car) {
        carsViewModel?.select(car)
    }
}