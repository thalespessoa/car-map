package com.cars.carsmap.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cars.carsmap.R
import com.cars.carsmap.SingleFragmentActivity
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@RunWith(AndroidJUnit4::class)
class ListFragmentTest {

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(SingleFragmentActivity::class.java)

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

    private val fakeList = listOf(
        Car("1", modelName = "Model name 1"),
        Car("2", modelName = "Model name 2"),
        Car("3", modelName = "Model name 3"),
        Car("4", modelName = "Model name 4"))

    @Before
    fun setUp() {
        listFragment = ListFragment()

        Mockito.`when`(viewModel.viewState).thenReturn(viewState)

        listFragment.viewModelFactory = TestViewModelFactory(viewModel)
        activityRule.activity.setFragment(listFragment)
    }

    @Test
    fun testCarList() {
        viewState.value = CarsViewState(ViewStateStatus.SUCCESS, fakeList)
        onView(withId(R.id.swipe_refresh_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_image)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
}

    @Test
    fun testEmptyStateSuccess() {
        viewState.value = CarsViewState(ViewStateStatus.SUCCESS)
        onView(withId(R.id.empty_image)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyStateError() {
        viewState.value = CarsViewState(ViewStateStatus.ERROR)
        onView(withId(R.id.empty_image)).check(matches(isDisplayed()))
    }

    @Test
    fun testProgressState() {
        viewState.value = CarsViewState(ViewStateStatus.PROGRESS)
        onView(withId(R.id.swipe_refresh_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_image)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }
}