package com.longle.presentation.ui.weather.mapper

import com.longle.domain.usecase.weatherforecast.model.Weather
import com.longle.presentation.di.FragmentScope
import com.longle.presentation.ui.weather.viewdata.WeatherViewData
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@FragmentScope
class WeatherViewDataMapper
@Inject constructor() {

    private val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

    fun mapToViewData(weather: Weather): WeatherViewData {
        return WeatherViewData(
            weather.cityId,
            weather.date.toFormattedDate(),
            weather.averageTemp.roundToInt().toString(),
            weather.pressure,
            weather.humidity.toString(),
            weather.description
        )
    }

    private fun Long.toFormattedDate(): String {
        return dateFormat.format(Date(this))
    }
}
