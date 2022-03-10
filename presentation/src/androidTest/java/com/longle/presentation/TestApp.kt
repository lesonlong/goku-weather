package com.longle.presentation

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.longle.presentation.util.AppTestRunner].
 */
class TestApp : Application()
