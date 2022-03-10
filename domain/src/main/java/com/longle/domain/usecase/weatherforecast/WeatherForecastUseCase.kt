package com.longle.domain.usecase.weatherforecast

import com.longle.domain.usecase.common.UseCase
import com.longle.domain.usecase.weatherforecast.model.Weather
import com.longle.domain.usecase.weatherforecast.result.WeatherForecastResult

/**
 * Weather UseCase
 *
 * @param <String> The Weather UseCase need a String Parameter to execute
 * @param <WeatherForecastResult> The Weather UseCase will be returning WeatherForecastResult cases
 * @param <List<Weather>> The Weather UseCase will be returning List<Weather> data
 */
interface WeatherForecastUseCase : UseCase<String, WeatherForecastResult, List<Weather>>
