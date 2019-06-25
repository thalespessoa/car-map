package com.cars.carsmap

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * This is a non-production activity to test fragments isolated
 */
class SingleFragmentActivity : AppCompatActivity() {

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment)
            .commit()
    }
}