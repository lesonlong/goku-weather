package com.longle.presentation.ui.weather.viewdata

data class WeatherViewData(
    val cityId: Int,
    val date: String,
    val averageTemp: String,
    val pressure: String,
    val humidity: String,
    val description: String
) {
    fun getTalkBackContentDescription(): String {
        return "Date $date " +
            "Average Temperature $averageTemp\u2103 " +
            "Pressure $pressure " +
            "Humidity $humidity% " +
            "Description $description"
    }
}
