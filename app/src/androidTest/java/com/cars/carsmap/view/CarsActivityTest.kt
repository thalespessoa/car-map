package com.cars.carsmap.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.cars.carsmap.R
import com.cars.carsmap.TestViewModelFactory
import com.cars.carsmap.model.entity.Car
import com.cars.carsmap.viewmodel.CarsViewModel
import com.cars.carsmap.viewmodel.CarsViewState
import com.cars.carsmap.viewmodel.ViewStateStatus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@RunWith(AndroidJUnit4::class)
class CarsActivityTest {

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(CarsActivity::class.java)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var viewModel: CarsViewModel

    private val viewState = MutableLiveData<CarsViewState>()

    private lateinit var listFragment: ListFragment

    private val fakeList = listOf(Car("1"), Car("2"), Car("2"))


    @Before
    fun setUp() {
        activityRule.activity.viewModelFactory = TestViewModelFactory(viewModel)
    }


//    @Test
//    fun testDifferentLayouts() {
////        if(isSplittedScreen(activity)) {
////            Espresso.onView(ViewMatchers.withId(R.id.tab_layout)).check(ViewAssertions.doesNotExist())
////            Espresso.onView(ViewMatchers.withId(R.id.view_pager)).check(ViewAssertions.doesNotExist())
////        } else {
////            Espresso.onView(ViewMatchers.withId(R.id.tab_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
////            Espresso.onView(ViewMatchers.withId(R.id.view_pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
////        }
//    }
//
//    @Test
//    fun testCarList() {
//        val viewState = CarsViewState(ViewStateStatus.SUCCESS, fakeList)
//        activity.onChanged(viewState)
////        onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }

    @Test
    fun testCarDetail() {
        viewState.value = CarsViewState(ViewStateStatus.SUCCESS, carSelected = Car("1"))
        onView(withId(R.id.view_detail)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
//
//    @Test
//    fun testCarOnMap() {
//        val viewState = CarsViewState(ViewStateStatus.SUCCESS, carSelectedMap = Car("1"))
//        activity.onChanged(viewState)
//    }
//
//    @Test
//    fun testErrorDialog() {
//        val viewState = CarsViewState(ViewStateStatus.ERROR, message = "Error message")
//        activity.onChanged(viewState)
//    }
//
//    private fun isSplittedScreen(context: Context) = isTablet(context) || !isPortrait(context)
//
//    private fun isTablet(context: Context): Boolean {
//        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
//    }
//
//    private fun isPortrait(context: Context): Boolean {
//        return context.resources.configuration.screenHeightDp > context.resources.configuration.screenWidthDp
//    }
}