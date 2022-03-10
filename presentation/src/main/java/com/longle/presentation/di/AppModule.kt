package com.longle.presentation.di

import android.app.Application
import android.content.Context
import com.longle.data.di.CoreDataModule
import com.longle.data.di.RepoModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepoModule::class, CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app
}
