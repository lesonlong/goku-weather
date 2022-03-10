package com.longle.presentation.util

import android.app.Application
import com.scottyab.rootbeer.RootBeer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RootedDeviceHelperImpl(private val app: Application) : RootedDeviceHelper {

    override suspend fun isRootedDevice(): Boolean {
        return withContext(Dispatchers.Default) {
            RootBeer(app).isRooted
        }
    }
}
