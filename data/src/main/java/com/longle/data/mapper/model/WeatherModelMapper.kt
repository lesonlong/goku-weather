package com.longle.data.mapper.model

import com.longle.data.model.local.WeatherEntity
import com.longle.domain.usecase.weatherforecast.model.Weather
import javax.inject.Inject

class WeatherModelMapper
@Inject constructor() : ModelMapper<List<WeatherEntity>, List<Weather>> {

    override fun toModel(input: List<WeatherEntity>): List<Weather> {
        return input.map { dto ->
            Weather(
                dto.cityId,
                dto.date,
                dto.averageTemp,
                dto.pressure,
                dto.humidity,
                dto.description
            )
        }
    }
}
