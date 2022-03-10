package com.longle.domain.repository

import com.longle.domain.model.Result
import com.longle.domain.model.Status
import com.longle.domain.usecase.weatherforecast.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeatherForecast(keyword: String): Flow<Result<Status, List<Weather>>>
}
