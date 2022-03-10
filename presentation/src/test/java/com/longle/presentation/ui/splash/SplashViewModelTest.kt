package com.longle.presentation.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.longle.presentation.util.CoroutineTestRule
import com.longle.presentation.util.RootedDeviceHelper
import com.longle.presentation.util.mock
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @Rule
    @JvmField
    var testCoroutinesScope = CoroutineTestRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var rootedDeviceHelper: RootedDeviceHelper

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        viewModel = SplashViewModel(rootedDeviceHelper)
    }

    @Test
    fun `SHOULD not null`() {
        MatcherAssert.assertThat(viewModel.shouldOpenApp, CoreMatchers.notNullValue())
    }

    @Test
    fun `SHOULD not open App WHEN run on rooted device`() = runBlockingTest {
        `when`(rootedDeviceHelper.isRootedDevice()).thenReturn(true)
        val observer = mock<Observer<Boolean>>()
        viewModel.shouldOpenApp.observeForever(observer)

        viewModel.checkRootedDevice()

        Mockito.verify(observer).onChanged(false)
    }

    @Test
    fun `SHOULD open App WHEN run on un-rooted device`() = runBlockingTest {
        `when`(rootedDeviceHelper.isRootedDevice()).thenReturn(false)
        val observer = mock<Observer<Boolean>>()
        viewModel.shouldOpenApp.observeForever(observer)

        viewModel.checkRootedDevice()

        Mockito.verify(observer).onChanged(true)
    }
}
