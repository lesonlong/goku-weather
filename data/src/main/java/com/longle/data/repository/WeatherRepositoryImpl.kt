package com.longle.data.repository

import com.longle.data.api.ApiService
import com.longle.data.db.WeatherDao
import com.longle.data.mapper.entity.WeatherEntityMapper
import com.longle.data.mapper.model.WeatherModelMapper
import com.longle.data.util.RateLimiter
import com.longle.domain.model.Result
import com.longle.domain.model.Status
import com.longle.domain.repository.WeatherRepository
import com.longle.domain.usecase.weatherforecast.model.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository module for handling data operations.
 */
class WeatherRepositoryImpl
@Inject constructor(
    private val weatherDao: WeatherDao,
    private val service: ApiService,
    private val weatherEntityMapper: WeatherEntityMapper,
    private val weatherModelMapper: WeatherModelMapper,
    private val rateLimiter: RateLimiter
) : WeatherRepository {

    companion object {
        private const val WEATHER_KEY_RATE_LIMITER = "WEATHER_KEY_RATE_LIMITER"
        private const val CNT = 7
        private const val UNITS = "metric"
        private const val APP_ID = "60c6fbeb4b93ac653c492ba806fc346d"
    }

    override fun getWeatherForecast(keyword: String): Flow<Result<Status, List<Weather>>> {
        return networkBoundResult(
            query = { weatherDao.getValidDateWeathers(keyword) },
            fetch = {
                service.getWeathers(keyword, CNT, UNITS, APP_ID)
            },
            shouldFetch = { it.isNullOrEmpty() || rateLimiter.shouldFetch(WEATHER_KEY_RATE_LIMITER) },
            saveFetchResult = {
                // Map Api Model to Database Model
                weatherEntityMapper.toEntity(it).let { list ->
                    weatherDao.updateAll(
                        list.map { dto ->
                            // Save keyword for search weather forecast offline
                            dto.keyword = keyword
                            dto
                        }
                    )
                }
            },
            mapper = {
                // Map Database Model to Domain Model
                it?.let { weatherModelMapper.toModel(it) }
            },
            onFetchFailed = { rateLimiter.reset(WEATHER_KEY_RATE_LIMITER) }
        )
    }
}
