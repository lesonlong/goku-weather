package com.longle.data.di

import com.longle.data.repository.WeatherRepositoryImpl
import com.longle.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepo(repo: WeatherRepositoryImpl): WeatherRepository
}
