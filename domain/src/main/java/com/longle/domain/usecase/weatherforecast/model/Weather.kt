package com.longle.domain.usecase.weatherforecast.model

data class Weather(
    val cityId: Int,
    val date: Long,
    val averageTemp: Float,
    val pressure: String,
    val humidity: Int,
    val description: String
)
