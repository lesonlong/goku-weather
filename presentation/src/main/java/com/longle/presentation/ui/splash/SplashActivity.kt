package com.longle.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.longle.presentation.R
import com.longle.presentation.databinding.ActivitySplashBinding
import com.longle.presentation.di.ViewModelFactory
import com.longle.presentation.ui.main.MainActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var factory: ViewModelFactory<SplashViewModel>

    private val viewModel: SplashViewModel by viewModels { factory }
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeUi()

        viewModel.checkRootedDevice()
    }

    private fun subscribeUi() {
        viewModel.shouldOpenApp.observe(this) { openApp ->
            if (openApp) {
                showMainActivity()
            } else {
                showRootedDeviceRejectedDialog()
            }
        }
    }

    private fun showMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showRootedDeviceRejectedDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.rooted_device_message)
            .setPositiveButton(R.string.close) { _, _ ->
                finish()
            }
            .setCancelable(false).show()
    }
}
