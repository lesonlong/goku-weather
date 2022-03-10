package com.longle.data.mapper.entity

import com.longle.data.model.remote.WeatherForecastDto
import com.longle.data.model.local.WeatherEntity
import com.longle.data.util.getWeatherDate
import javax.inject.Inject

class WeatherEntityMapper
@Inject constructor() : EntityMapper<WeatherForecastDto, List<WeatherEntity>> {

    override fun toEntity(input: WeatherForecastDto): List<WeatherEntity> {
        return input.weathers.mapIndexed { index, dto ->
            WeatherEntity(
                cityId = input.city.id,
                date = getWeatherDate(index),
                averageTemp = dto.temperature.day,
                pressure = dto.pressure,
                humidity = dto.humidity,
                description = dto.weatherDescription[0].description
            )
        }
    }
}
