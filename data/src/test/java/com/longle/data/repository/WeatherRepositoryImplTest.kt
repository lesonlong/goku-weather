package com.longle.data.repository

import com.longle.data.api.ApiService
import com.longle.data.db.WeatherDao
import com.longle.data.mapper.entity.WeatherEntityMapper
import com.longle.data.mapper.model.WeatherModelMapper
import com.longle.data.model.remote.*
import com.longle.data.model.local.WeatherEntity
import com.longle.data.util.RateLimiter
import com.longle.domain.model.Status
import com.longle.domain.usecase.weatherforecast.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryImplTest {

    @Mock
    private lateinit var weatherDao: WeatherDao

    @Mock
    private lateinit var serviceApi: ApiService

    private val weatherEntityMapper = WeatherEntityMapper()
    private val weatherModelMapper = WeatherModelMapper()
    private val rateLimiter = RateLimiter(5, TimeUnit.SECONDS)

    private lateinit var repo: WeatherRepositoryImpl

    @Before
    fun setup() {
        repo = WeatherRepositoryImpl(
            weatherDao,
            serviceApi,
            weatherEntityMapper,
            weatherModelMapper,
            rateLimiter
        )
    }

    @Test
    fun `SHOULD return success WHEN the forecast api got success code`() = runBlocking {
        Mockito.`when`(weatherDao.getValidDateWeathers("ho chi minh city"))
            .thenReturn(MutableStateFlow(getMockWeatherEntities()))
        Mockito.`when`(serviceApi.getWeathers(anyString(), anyInt(), anyString(), anyString()))
            .thenReturn(Response.success(getMockWeatherDtos()))

        val result = repo.getWeatherForecast("ho chi minh city").first {
            it.state !is Status.Loading
        }

        assertTrue(result.state is Status.Success)
        assertTrue(
            weatherEntityMapper.toEntity(getMockWeatherDtos()).let { list ->
                weatherModelMapper.toModel(list)
            }.equalWith(result.data)
        )
    }

    @Test
    fun `SHOULD return error WHEN the forecast api got error code`() = runBlocking {
        Mockito.`when`(weatherDao.getValidDateWeathers("ho chi minh city"))
            .thenReturn(MutableStateFlow(emptyList()))
        Mockito.`when`(serviceApi.getWeathers(anyString(), anyInt(), anyString(), anyString()))
            .thenReturn(Response.error(404, "City not found error".toResponseBody()))

        val result = repo.getWeatherForecast("ho chi minh city").first {
            it.state !is Status.Loading
        }

        assertTrue(result.state is Status.Error)
        assertEquals(emptyList<Weather>(), result.data)
    }

    private fun getMockWeatherEntities() = listOf(
        WeatherEntity(
            "ho chi minh city",
            1,
            1635705585544,
            23f,
            "1013",
            64,
            "light rain"
        )
    )

    private fun getMockWeatherDtos() = WeatherForecastDto(
        CityDto(1),
        listOf(
            WeatherDto(
                TemperatureDto(23f),
                "1013",
                64,
                listOf(
                    WeatherDescriptionDto(
                        "light rain"
                    )
                )
            )
        )
    )

    private fun List<Weather>.equalWith(other: List<Weather>?): Boolean {
        if (this.size != other?.size) return false
        for ((index, item) in this.withIndex()) {
            if (item.cityId != other[index].cityId &&
                item.averageTemp != other[index].averageTemp &&
                item.pressure != other[index].pressure &&
                item.humidity != other[index].humidity &&
                item.description != other[index].description
            ) return false
        }
        return true
    }
}
