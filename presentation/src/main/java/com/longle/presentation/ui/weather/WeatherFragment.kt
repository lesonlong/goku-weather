package com.longle.presentation.ui.weather

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.longle.presentation.R
import com.longle.presentation.databinding.FragmentWeatherBinding
import com.longle.presentation.di.Injectable
import com.longle.presentation.di.ViewModelFactory
import com.longle.presentation.util.autoCleared
import com.longle.presentation.util.dismissKeyboard
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<WeatherViewModel>

    private val viewModel: WeatherViewModel by viewModels { factory }
    private var binding by autoCleared<FragmentWeatherBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        subscribeUi()
    }

    private fun setUpView(view: View) {
        binding = FragmentWeatherBinding.bind(view)
        binding?.recyclerView?.adapter = WeatherAdapter()
        initSearchInputListener()
    }

    private fun subscribeUi() {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        viewModel.city.observe(viewLifecycleOwner) {
            clearFocusAndCloseKeyboard()
        }
    }

    private fun initSearchInputListener() {
        binding?.inputCity?.setOnEditorActionListener { _, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadWeather()
                true
            } else {
                false
            }
        }
        binding?.inputCity?.setOnKeyListener { _, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                loadWeather()
                true
            } else {
                false
            }
        }
    }

    private fun loadWeather() {
        val city = binding?.inputCity?.text?.toString()
        viewModel.loadWeather(city)
    }

    private fun clearFocusAndCloseKeyboard() {
        // Clear input field focus
        activity?.currentFocus?.clearFocus()
        // Dismiss keyboard
        binding?.inputCity?.let { dismissKeyboard(context, it.windowToken) }
    }
}
