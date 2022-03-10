package com.longle.data.api

import com.longle.data.model.remote.WeatherForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface ApiService {

    companion object {
        const val DOMAIN = "api.openweathermap.org"
        const val ENDPOINT = "https://$DOMAIN/"

        const val PIN1 = "sha256/axmGTWYycVN5oCjh3GJrxWVndLSZjypDO6evrHMwbXg="
        const val PIN2 = "sha256/4a6cPehI7OG6cuDZka5NDZ7FR8a60d3auda+sKfg4Ng="
        const val PIN3 = "sha256/x4QzPSC810K5/cMjb05Qm4k3Bw5zBn4lTdO/nEW/Td4="
    }

    @GET("data/2.5/forecast/daily")
    suspend fun getWeathers(
        @Query("q") city: String,
        @Query("cnt") cnt: Int,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<WeatherForecastDto>
}
