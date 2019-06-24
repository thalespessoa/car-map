package com.cars.carsmap.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.cars.carsmap.R
import com.cars.carsmap.ViewModelFactory
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.view.adapter.ViewPagerAdapter
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import com.cars.carsmap.viewmodel.ViewStateStatus
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

/**
 * Main class, responsible to present ListFragment and MapFragment as a ViewPager in case of phone portrait
 * or present ListFragment and MapFragment as splitted screen in case of phone landscape and tablet both orientation.
 * This class is also responsible for the navigation between the fragments when it's presented as ViewPager
 *
 * All possible states are represented by a unique and immutable CarsViewState inside the CarsViewModel
 *
 * @see CarsViewState
 * @see CarsViewModel
 */
class CarsActivity : AppCompatActivity(), Observer<CarsViewState> {

    companion object {
        private const val TAG_DETAIL = "detail"
    }

    private lateinit var viewModel:CarsViewModel

    private val tabLayout: TabLayout? by lazy { findViewById<TabLayout>(R.id.tab_layout) }
    private val viewPager: ViewPager? by lazy { findViewById<ViewPager>(R.id.view_pager) }
    private val appBarLayout: AppBarLayout? by lazy { findViewById<AppBarLayout>(R.id.app_bar_layout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(CarsViewModel::class.java)
        viewModel.viewState.observe(this, this)
        viewModel.refresh()

        viewPager?.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        tabLayout?.getTabAt(0)?.setIcon(R.drawable.baseline_list_24px)
        tabLayout?.getTabAt(1)?.setIcon(R.drawable.baseline_map_24px)
    }

    override fun onChanged(viewState: CarsViewState?) {
        viewState?.carSelectedMap?.also {
            if (viewPager?.currentItem == 0) {
                viewPager?.currentItem = 1
                appBarLayout?.setExpanded(true, true)
            }
        }

        handleDetailFragment(viewState?.carSelected)

        if (viewState?.status == ViewStateStatus.ERROR)
            handleErrorState(viewState)
    }

    override fun onBackPressed() {
        when {
            viewPager?.currentItem == 1 -> viewPager?.currentItem = 0
            else -> super.onBackPressed()
        }
    }

    private fun handleErrorState(viewState: CarsViewState) {
        if (viewState.message?.isNotBlank() == true)
            AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_error_outline_24px)
                .setTitle(R.string.error_title)
                .setMessage(viewState.message)
                .setOnDismissListener { viewModel?.readErrorMessage() }
                .setPositiveButton(R.string.ok, null)
                .show()

    }

    private fun handleDetailFragment(car: Car?) {
        val detailFragment = (supportFragmentManager.findFragmentByTag(TAG_DETAIL) as DetailDialogFragment?)
        car?.let {
            detailFragment ?: DetailDialogFragment().show(supportFragmentManager, TAG_DETAIL)
        } ?: detailFragment?.dismiss()
    }
}
