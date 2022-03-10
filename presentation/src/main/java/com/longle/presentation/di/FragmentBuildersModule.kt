package com.longle.presentation.di

import com.longle.presentation.ui.weather.WeatherFragment
import com.longle.presentation.ui.weather.di.WeatherFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [WeatherFragmentModule::class])
    abstract fun contributeWeatherFragment(): WeatherFragment
}
