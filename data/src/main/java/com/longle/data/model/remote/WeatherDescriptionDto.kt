package com.longle.data.model.remote

import com.google.gson.annotations.SerializedName

data class WeatherDescriptionDto(
    @SerializedName("description")
    val description: String
)
