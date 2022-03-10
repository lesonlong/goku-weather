package com.longle.data.util

import java.util.Calendar

/**
 * get Weather Date for Forecast, index = 0 is current weather date
 */
fun getWeatherDate(index: Int = 0) = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis + index * (24 * 60 * 60 * 1000)
