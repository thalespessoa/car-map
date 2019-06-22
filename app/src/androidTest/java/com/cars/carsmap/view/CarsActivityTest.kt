package com.cars.carsmap.view

import android.content.Context
import android.content.res.Configuration
import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.cars.carsmap.R
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.viewmodel.CarsViewState
import com.cars.carsmap.viewmodel.ViewStateStatus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class CarsActivityTest {

    @get:Rule
    var activityActivityTestRule = ActivityTestRule<CarsActivity>(CarsActivity::class.java)

    private val fakeList = listOf(Car("1"), Car("2"), Car("2"))

    private lateinit var activity:CarsActivity

    @Before
    fun setUp() {
        activity = activityActivityTestRule.activity
    }

    @Test
    fun testDifferentLayouts() {
        if(isSplittedScreen(activity)) {
            Espresso.onView(ViewMatchers.withId(R.id.tab_layout)).check(ViewAssertions.doesNotExist())
            Espresso.onView(ViewMatchers.withId(R.id.view_pager)).check(ViewAssertions.doesNotExist())
        } else {
            Espresso.onView(ViewMatchers.withId(R.id.tab_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(ViewMatchers.withId(R.id.view_pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun testCarList() {
        val viewState = CarsViewState(ViewStateStatus.SUCCESS, fakeList)
        activity.onChanged(viewState)
        onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testCarDetail() {
        val viewState = CarsViewState(ViewStateStatus.SUCCESS, carSelected = Car("1"))
        activity.onChanged(viewState)
        onView(withId(R.id.view_detail)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    @UiThreadTest
    fun testCarOnMap() {
        val viewState = CarsViewState(ViewStateStatus.SUCCESS, carSelectedMap = Car("1"))
        activity.onChanged(viewState)
    }

    @Test
    @UiThreadTest
    fun testErrorDialog() {
        val viewState = CarsViewState(ViewStateStatus.ERROR, message = "Error message")
        activity.onChanged(viewState)
    }

    private fun isSplittedScreen(context: Context) = isTablet(context) || !isPortrait(context)

    private fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    private fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.screenHeightDp > context.resources.configuration.screenWidthDp
    }
}