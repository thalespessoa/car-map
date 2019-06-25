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
import kotlinx.android.synthetic.main.fragment_car_detail.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@RunWith(AndroidJUnit4::class)
class DetailDialogFragmentTest {

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

    private lateinit var fragment: DetailDialogFragment

    private val fakeCar = Car(
        "1",
        modelName = "Model name",
        color = "color_name",
        name = "Name",
        group = "Group",
        series = "Series"
    )

    @Before
    fun setUp() {
        fragment = DetailDialogFragment()

        Mockito.`when`(viewModel.viewState).thenReturn(viewState)

        fragment.viewModelFactory = TestViewModelFactory(viewModel)
        activityRule.activity.setFragment(fragment)
    }

    @Test
    fun testCarDetail() {
        viewState.value = CarsViewState(ViewStateStatus.SUCCESS, carSelected = fakeCar)
        onView(withId(R.id.title)).check(matches(withText(fakeCar.modelName)))
        onView(withId(R.id.color)).check(matches(withText(fakeCar.readableColor)))
        onView(withId(R.id.name)).check(matches(withText(fakeCar.name)))
        onView(withId(R.id.group)).check(matches(withText(fakeCar.group)))
        onView(withId(R.id.serie)).check(matches(withText(fakeCar.series)))
    }
}