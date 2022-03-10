package com.longle.domain.usecase.weatherforecast

import com.longle.domain.model.Result
import com.longle.domain.model.Status
import com.longle.domain.model.StatusCode
import com.longle.domain.repository.WeatherRepository
import com.longle.domain.usecase.weatherforecast.model.Weather
import com.longle.domain.usecase.weatherforecast.result.WeatherForecastResult
import com.longle.domain.util.asFlow
import com.longle.domain.util.toUseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class WeatherForecastUseCaseImpl(private val weatherRepository: WeatherRepository) :
    WeatherForecastUseCase {

    companion object {
        private const val MIN_LENGTH = 3
    }

    override fun execute(param: String): Flow<Result<WeatherForecastResult, List<Weather>>> {
        val city = param.trim().lowercase(Locale.getDefault())
        return if (isCityValid(city)) {
            weatherRepository.getWeatherForecast(city).map { result ->
                when (result.state) {
                    is Status.Loading -> {
                        result.toUseCaseResult(WeatherForecastResult.LOADING)
                    }
                    is Status.Success -> {
                        result.toUseCaseResult(WeatherForecastResult.SUCCESS)
                    }
                    is Status.Error -> {
                        when (result.state.code) {
                            StatusCode.NotFound -> {
                                result.toUseCaseResult(WeatherForecastResult.CITY_NOT_FOUND_ERROR)
                            }
                            StatusCode.NoNetwork -> {
                                result.toUseCaseResult(WeatherForecastResult.NO_NETWORK_ERROR)
                            }
                            else -> { // default error case
                                result.toUseCaseResult(WeatherForecastResult.ERROR)
                            }
                        }
                    }
                }
            }
        } else {
            Result(WeatherForecastResult.CITY_MIN_LENGTH_ERROR, null).asFlow()
        }
    }

    private fun isCityValid(city: String): Boolean {
        return city.length >= MIN_LENGTH
    }
}
