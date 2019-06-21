package com.cars.carsmap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cars.carsmap.ListBinding
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.view.adapter.CarsListAdapter
import com.cars.carsmap.viewmodel.CarsViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private val recyclerView: RecyclerView by lazy { recycler_view }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { swipe_refresh_layout }
    private val carsAdapter = CarsListAdapter()

    private val carsViewModel by lazy {
        activity?.let { ViewModelProviders.of(it, ViewModelFactory()).get(CarsViewModel::class.java) }
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

        carsAdapter.onCarSelected = { carsViewModel?.select(it) }
        carsAdapter.onCarPlaceSelected = { carsViewModel?.selectOnMap(it) }
    }

    override fun onRefresh() {
        carsViewModel?.refresh()
        swipeRefreshLayout.isRefreshing = false
    }
}