package com.longle.presentation.ui.weather.di

import android.content.Context
import com.longle.domain.repository.WeatherRepository
import com.longle.domain.usecase.weatherforecast.WeatherForecastUseCase
import com.longle.domain.usecase.weatherforecast.WeatherForecastUseCaseImpl
import com.longle.presentation.R
import com.longle.presentation.di.FragmentScope
import com.longle.presentation.ui.weather.viewdata.WeatherText
import dagger.Module
import dagger.Provides

@Module
class WeatherFragmentModule {

    @FragmentScope
    @Provides
    fun provideWeatherUseCase(weatherRepository: WeatherRepository): WeatherForecastUseCase {
        return WeatherForecastUseCaseImpl(weatherRepository)
    }

    @FragmentScope
    @Provides
    fun provideWeatherText(app: Context): WeatherText {
        return WeatherText(
            app.getString(R.string.city_min_length_error),
            app.getString(R.string.city_not_found_error),
            app.getString(R.string.no_network_error),
            app.getString(R.string.unknown_error)
        )
    }
}
