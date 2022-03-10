package com.longle.domain.usecase.weatherforecast

import com.longle.domain.model.Result
import com.longle.domain.model.StatusCode
import com.longle.domain.repository.WeatherRepository
import com.longle.domain.usecase.weatherforecast.model.Weather
import com.longle.domain.usecase.weatherforecast.result.WeatherForecastResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherForecastUseCaseImplTest {

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    /** Under testing */
    private lateinit var useCase: WeatherForecastUseCaseImpl

    @Before
    fun setUp() {
        useCase = WeatherForecastUseCaseImpl(weatherRepository)
    }

    @Test
    fun `SHOULD return weather forecast loading WHEN it just starts executing`() = runBlocking {
        val dataResult = Result.loading(null)
        Mockito.`when`(weatherRepository.getWeatherForecast("ho chi minh city"))
            .thenReturn(MutableStateFlow(dataResult))

        val result = useCase.execute("ho chi minh city").first()

        Assert.assertEquals(WeatherForecastResult.LOADING, result.state)
    }

    @Test
    fun `SHOULD return weather forecast success WHEN repository got success`() = runBlocking {
        val data = getWeatherList()
        val dataResult = Result.success(StatusCode.Ok, data)
        Mockito.`when`(weatherRepository.getWeatherForecast("hoi an"))
            .thenReturn(MutableStateFlow(dataResult))

        val result = useCase.execute("hoi an").first()

        Assert.assertEquals(WeatherForecastResult.SUCCESS, result.state)
        Assert.assertEquals(result.data, data)
    }

    @Test
    fun `SHOULD return the city name min-length error WHEN city name length less than 3 characters`() =
        runBlocking {
            val result = useCase.execute("da       ").first()

            Assert.assertEquals(WeatherForecastResult.CITY_MIN_LENGTH_ERROR, result.state)
            Assert.assertEquals(result.data, null)
        }

    @Test
    fun `SHOULD return weather forecast city not found error WHEN repository has city not found error`() =
        runBlocking {
            val dataResult = Result.error(StatusCode.NotFound, null, "city not found")
            Mockito.`when`(weatherRepository.getWeatherForecast("da nang"))
                .thenReturn(MutableStateFlow(dataResult))

            val result = useCase.execute("da nang").first()

            Assert.assertEquals(WeatherForecastResult.CITY_NOT_FOUND_ERROR, result.state)
            Assert.assertEquals(result.data, null)
        }

    @Test
    fun `SHOULD return weather forecast no network error WHEN repository has no network error`() =
        runBlocking {
            val dataResult = Result.error(StatusCode.NoNetwork, null, "no network")
            Mockito.`when`(weatherRepository.getWeatherForecast("rach gia"))
                .thenReturn(MutableStateFlow(dataResult))

            val result = useCase.execute("rach gia").first()

            Assert.assertEquals(WeatherForecastResult.NO_NETWORK_ERROR, result.state)
            Assert.assertEquals(result.data, null)
        }

    @Test
    fun `SHOULD return weather forecast default error WHEN repository has unknown error`() =
        runBlocking {
            val dataResult = Result.error(StatusCode.Unknown, null, "unknown error")
            Mockito.`when`(weatherRepository.getWeatherForecast("bien hoa"))
                .thenReturn(MutableStateFlow(dataResult))

            val result = useCase.execute("bien hoa").first()

            Assert.assertEquals(WeatherForecastResult.ERROR, result.state)
            Assert.assertEquals(result.data, null)
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
