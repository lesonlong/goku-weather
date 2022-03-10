package com.longle.data.model.remote

import com.google.gson.annotations.SerializedName

data class TemperatureDto(
    @SerializedName("day")
    val day: Float
)
