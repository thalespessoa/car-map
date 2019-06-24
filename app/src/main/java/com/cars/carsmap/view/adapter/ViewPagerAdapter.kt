package com.cars.carsmap.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cars.carsmap.view.ListFragment
import com.cars.carsmap.view.MapFragment

/**
 * ViewPagerAdapter used by phone portrait layout, contains a ListFragment and MapFragment
 *
 * @see ListFragment
 * @see MapFragment
 */
class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = arrayListOf(ListFragment(), MapFragment())
    private val titles = arrayListOf("List", "Map")

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = 2

}