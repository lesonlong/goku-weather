package com.longle.presentation.ui.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.longle.domain.model.Result
import com.longle.domain.usecase.weatherforecast.WeatherForecastUseCase
import com.longle.domain.usecase.weatherforecast.model.Weather
import com.longle.domain.usecase.weatherforecast.result.WeatherForecastResult
import com.longle.presentation.ui.weather.mapper.WeatherViewDataMapper
import com.longle.presentation.ui.weather.viewdata.WeatherText
import com.longle.presentation.ui.weather.viewdata.WeatherViewData
import com.longle.presentation.util.CoroutineTestRule
import com.longle.presentation.util.mock
import com.longle.presentation.util.toViewData
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @Rule
    @JvmField
    var testCoroutinesScope = CoroutineTestRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherForecastUseCase: WeatherForecastUseCase

    private val viewDataMapper = WeatherViewDataMapper()
    private val weatherText = WeatherText(
        "The city name must be from 3 characters",
        "City Not Found",
        "No Network",
        "Unknown Error"
    )
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        viewModel =
            spy(WeatherViewModel(weatherText, weatherForecastUseCase, WeatherViewDataMapper()))
    }

    @Test
    fun `SHOULD not null`() {
        MatcherAssert.assertThat(viewModel.city, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(viewModel.weathers, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(viewModel.errorText, CoreMatchers.notNullValue())
        Mockito.verify(weatherForecastUseCase, Mockito.never()).execute(Mockito.anyString())
        viewModel.loadWeather("ho chi minh city")
        Mockito.verify(weatherForecastUseCase, Mockito.never()).execute(Mockito.anyString())
    }

    @Test
    fun `SHOULD execute weather forecast business WHEN the city name is inputted`() {
        viewModel.weathers.observeForever(mock())
        Mockito.verifyNoMoreInteractions(weatherForecastUseCase)
        viewModel.loadWeather("ho chi minh city")
        Mockito.verify(weatherForecastUseCase).execute("ho chi minh city")
        Mockito.verifyNoMoreInteractions(weatherForecastUseCase)
    }

    @Test
    fun `SHOULD display weather forecast WHEN weather forecast business is finished with success`() {
        val result = Result(WeatherForecastResult.SUCCESS, getWeatherList())
        Mockito.`when`(weatherForecastUseCase.execute(Mockito.anyString()))
            .thenReturn(MutableStateFlow(result))
        val observer = mock<Observer<Result<WeatherForecastResult, List<WeatherViewData>>>>()
        viewModel.weathers.observeForever(observer)

        viewModel.loadWeather("ho chi minh city")

        Mockito.verify(weatherForecastUseCase).execute("ho chi minh city")
        Mockito.verify(observer)
            .onChanged(result.toViewData { weathers -> weathers?.map(viewDataMapper::mapToViewData) })
        Assert.assertEquals(WeatherForecastResult.SUCCESS, viewModel.weathers.value?.state)
    }

    @Test
    fun `SHOULD display city not found error message WHEN weather forecast business is finished with an city not found error`() {
        val result = Result(WeatherForecastResult.CITY_NOT_FOUND_ERROR, null)
        Mockito.`when`(weatherForecastUseCase.execute(Mockito.anyString()))
            .thenReturn(MutableStateFlow(result))
        val observer = mock<Observer<Result<WeatherForecastResult, List<WeatherViewData>>>>()
        viewModel.weathers.observeForever(observer)
        val observerMessage = mock<Observer<String>>()
        viewModel.errorText.observeForever(observerMessage)

        viewModel.loadWeather("hoi an")

        Mockito.verify(weatherForecastUseCase).execute("hoi an")
        Mockito.verify(observer).onChanged(result)
        Mockito.verify(observerMessage).onChanged(weatherText.cityNotFoundError)
        Assert.assertEquals(
            WeatherForecastResult.CITY_NOT_FOUND_ERROR,
            viewModel.weathers.value?.state
        )
    }

    @Test
    fun `SHOULD display no network error message WHEN weather forecast business is finished with an no network error`() {
        val result = Result(WeatherForecastResult.NO_NETWORK_ERROR, null)
        Mockito.`when`(weatherForecastUseCase.execute(Mockito.anyString()))
            .thenReturn(MutableStateFlow(result))
        val observer = mock<Observer<Result<WeatherForecastResult, List<WeatherViewData>>>>()
        viewModel.weathers.observeForever(observer)
        val observerMessage = mock<Observer<String>>()
        viewModel.errorText.observeForever(observerMessage)

        viewModel.loadWeather("rach gia")

        Mockito.verify(weatherForecastUseCase).execute("rach gia")
        Mockito.verify(observer).onChanged(result)
        Mockito.verify(observerMessage).onChanged(weatherText.noNetworkError)
        Assert.assertEquals(WeatherForecastResult.NO_NETWORK_ERROR, viewModel.weathers.value?.state)
    }

    @Test
    fun `SHOULD display default error message WHEN weather forecast business is finished with an unknown error`() {
        val result = Result(WeatherForecastResult.ERROR, null)
        Mockito.`when`(weatherForecastUseCase.execute(Mockito.anyString()))
            .thenReturn(MutableStateFlow(result))
        val observer = mock<Observer<Result<WeatherForecastResult, List<WeatherViewData>>>>()
        viewModel.weathers.observeForever(observer)
        val observerMessage = mock<Observer<String>>()
        viewModel.errorText.observeForever(observerMessage)

        viewModel.loadWeather("ha noi city")

        Mockito.verify(weatherForecastUseCase).execute("ha noi city")
        Mockito.verify(observer).onChanged(result)
        Mockito.verify(observerMessage).onChanged(weatherText.defaultError)
        Assert.assertEquals(WeatherForecastResult.ERROR, viewModel.weathers.value?.state)
    }

    @Test
    fun `SHOULD load weather forecast again WHEN retry button is clicked`() {
        viewModel.onRetry("ho chi minh city")
        Mockito.verify(viewModel).loadWeather("ho chi minh city")
    }

    private fun getWeatherList() = listOf(
        Weather(
            1,
            1635705585544,
            23f,
            "1011",
            64,
            "moderate rain"
        ),
        Weather(
            2,
            1635705585544,
            25f,
            "1013",
            62,
            "light rain"
        )
    )
}
