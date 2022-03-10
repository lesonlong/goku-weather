package com.longle.presentation.di

import com.longle.presentation.ui.main.MainActivity
import com.longle.presentation.ui.splash.SplashActivity
import com.longle.presentation.ui.splash.di.SplashActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
