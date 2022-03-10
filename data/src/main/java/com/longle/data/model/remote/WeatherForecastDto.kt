package com.longle.data.model.remote

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("city")
    val city: CityDto,
    @SerializedName("list")
    val weathers: List<WeatherDto>
)
