package com.longle.presentation.ui.weather

import androidx.lifecycle.*
import com.longle.domain.usecase.weatherforecast.WeatherForecastUseCase
import com.longle.domain.usecase.weatherforecast.result.WeatherForecastResult
import com.longle.presentation.di.FragmentScope
import com.longle.presentation.testing.OpenForTesting
import com.longle.presentation.ui.weather.mapper.WeatherViewDataMapper
import com.longle.presentation.ui.weather.viewdata.WeatherText
import com.longle.presentation.util.toViewData
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * The ViewModel for [WeatherFragment].
 */
@OpenForTesting
@FragmentScope
class WeatherViewModel @Inject constructor(
    private val weatherText: WeatherText,
    private val weatherForecastUseCase: WeatherForecastUseCase,
    private val viewDataMapper: WeatherViewDataMapper
) : ViewModel() {

    private val _city = MutableLiveData<String>()
    private val _inputErrorText = MutableLiveData<String?>()
    private val _errorText = MutableLiveData<String>()

    val city: LiveData<String>
        get() = _city

    val inputErrorText: LiveData<String?>
        get() = _inputErrorText

    val errorText: LiveData<String>
        get() = _errorText

    val weathers = _city.asFlow()
        .flatMapLatest { city -> weatherForecastUseCase.execute(city) }
        .map { result ->
            // Error message that is displayed to user
            when (result.state) {
                WeatherForecastResult.CITY_MIN_LENGTH_ERROR -> {
                    _inputErrorText.value = weatherText.inputCityMinLengthError
                }
                WeatherForecastResult.CITY_NOT_FOUND_ERROR -> {
                    _errorText.value = weatherText.cityNotFoundError
                }
                WeatherForecastResult.NO_NETWORK_ERROR -> {
                    _errorText.value = weatherText.noNetworkError
                }
                WeatherForecastResult.ERROR -> {
                    _errorText.value = weatherText.defaultError
                }
            }
            // Map from Domain model to Presentation ViewData model
            result.toViewData { weathers -> weathers?.map(viewDataMapper::mapToViewData) }
        }.asLiveData(timeoutInMs = Long.MAX_VALUE)

    fun loadWeather(city: String?) {
        _inputErrorText.value = null
        _city.value = city ?: ""
    }

    fun onRetry(city: String?) {
        loadWeather(city)
    }
}
