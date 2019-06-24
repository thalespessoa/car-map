package com.cars.carsmap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cars.carsmap.ListBinding
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.view.adapter.CarsListAdapter
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import com.cars.carsmap.viewmodel.ViewStateStatus
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Fragment responsible to show the cars list (CarsViewState.list) on the list
 *
 * All possible states are represented by a unique and immutable CarsViewState inside the CarsViewModel
 *
 * @see CarsViewState
 * @see CarsViewModel
 */
class ListFragment : Fragment() {

    private val recyclerView: RecyclerView by lazy { recycler_view }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { swipe_refresh_layout }
    private val emptyImage by lazy { empty_image }
    private val retryButton:Button by lazy { retry_button }
    private val carsAdapter = CarsListAdapter()

    @VisibleForTesting
    var viewModelFactory = ViewModelFactory()

    private lateinit var carsViewModel:CarsViewModel

    //----------------------------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carsViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(CarsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<ListBinding>(inflater, R.layout.fragment_list, container, false)
            .apply {
                lifecycleOwner = this@ListFragment
                viewModel = carsViewModel
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = carsAdapter

        swipeRefreshLayout.setOnRefreshListener { carsViewModel.refresh() }
        carsAdapter.onCarSelected = { carsViewModel.select(it) }
        carsAdapter.onCarPlaceSelected = { carsViewModel.selectOnMap(it) }
        retryButton.setOnClickListener { carsViewModel.refresh() }

        carsViewModel.viewState.observe(this, Observer {
            val isProgress = it?.status == ViewStateStatus.PROGRESS
            val listIsEmpty = it?.list?.isEmpty() == true
            swipeRefreshLayout.isRefreshing = isProgress
            emptyImage.visibility = if(listIsEmpty && !isProgress) View.VISIBLE else View.INVISIBLE
        })
    }
}