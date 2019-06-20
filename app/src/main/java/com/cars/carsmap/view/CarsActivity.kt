package com.cars.carsmap.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState

class CarsActivity : AppCompatActivity(), Observer<CarsViewState> {

    companion object {
        private const val TAG_LIST = "list"
        private const val TAG_MAP = "map"
        private const val TAG_DETAIL = "detail"
    }

    private val viewModel by lazy { ViewModelProviders.of(this, ViewModelFactory()).get(CarsViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)

        if (supportFragmentManager.findFragmentByTag(TAG_LIST) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_list, ListFragment(), TAG_LIST)
                .commitAllowingStateLoss()
        }

        viewModel.viewState.observe(this, this)
        viewModel.refresh()
    }

    override fun onChanged(viewState: CarsViewState?) {
        viewState?.carSelected?.let {
            if (supportFragmentManager.findFragmentByTag(TAG_DETAIL) == null)
                DetailDialogFragment().show(supportFragmentManager, TAG_DETAIL)
        }
    }
}
