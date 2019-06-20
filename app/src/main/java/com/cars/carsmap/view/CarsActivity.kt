package com.cars.carsmap.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import com.google.android.material.tabs.TabLayout

class CarsActivity : AppCompatActivity(), Observer<CarsViewState> {

    companion object {
        private const val TAG_DETAIL = "detail"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(CarsViewModel::class.java).also {
            it.viewState.observe(this, this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun onChanged(viewState: CarsViewState?) {
        viewState?.carSelected?.let {
            if (supportFragmentManager.findFragmentByTag(TAG_DETAIL) == null)
                DetailDialogFragment().show(supportFragmentManager, TAG_DETAIL)
        }
    }
}
