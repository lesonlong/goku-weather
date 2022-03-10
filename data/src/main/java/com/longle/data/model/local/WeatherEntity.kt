package com.longle.data.model.local

import androidx.room.Entity

@Entity(primaryKeys = ["keyword", "cityId", "date"])
data class WeatherEntity(
    val cityId: Int,
    val date: Long,
    val averageTemp: Float,
    val pressure: String,
    val humidity: Int,
    val description: String
) {
    var keyword: String = ""
}
