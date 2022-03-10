package com.longle.presentation.ui.splash.di

import android.app.Application
import com.longle.presentation.di.ActivityScope
import com.longle.presentation.util.RootedDeviceHelper
import com.longle.presentation.util.RootedDeviceHelperImpl
import dagger.Module
import dagger.Provides

@Module
class SplashActivityModule {

    @ActivityScope
    @Provides
    fun provideRootedDeviceHelper(app: Application): RootedDeviceHelper {
        return RootedDeviceHelperImpl(app)
    }
}
