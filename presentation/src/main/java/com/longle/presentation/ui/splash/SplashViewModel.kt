package com.longle.presentation.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.longle.presentation.di.ActivityScope
import com.longle.presentation.util.RootedDeviceHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel used in [SplashActivity].
 */
@ActivityScope
class SplashViewModel @Inject constructor(
    private val rootedDevice: RootedDeviceHelper
) : ViewModel() {

    private val _shouldOpenApp = LiveEvent<Boolean>()

    val shouldOpenApp: LiveData<Boolean>
        get() = _shouldOpenApp

    fun checkRootedDevice() {
        viewModelScope.launch {
            val isRooted = rootedDevice.isRootedDevice()
            _shouldOpenApp.value = !isRooted
        }
    }
}
