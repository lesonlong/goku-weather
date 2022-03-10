package com.longle.data.model.remote

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("temp")
    val temperature: TemperatureDto,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("weather")
    val weatherDescription: List<WeatherDescriptionDto>
)
