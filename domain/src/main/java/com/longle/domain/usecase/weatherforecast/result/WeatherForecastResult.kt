package com.longle.domain.usecase.weatherforecast.result

/**
 * WeatherForecastResult that is provided to the Presentation module.
 *
 * These are usually created by the UseCase classes where they return
 * `Flow<Result<WeatherForecastResult, Data>>` to pass back the latest data to the Presentation Module with its fetch WeatherForecastResult.
 */
enum class WeatherForecastResult {
    LOADING,
    SUCCESS,
    CITY_MIN_LENGTH_ERROR,
    CITY_NOT_FOUND_ERROR,
    NO_NETWORK_ERROR,
    ERROR
}
