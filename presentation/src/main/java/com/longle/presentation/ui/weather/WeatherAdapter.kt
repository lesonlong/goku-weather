package com.longle.presentation.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.longle.presentation.databinding.WeatherItemBinding
import com.longle.presentation.ui.common.DataBoundListAdapter
import com.longle.presentation.ui.weather.viewdata.WeatherViewData

class WeatherAdapter : DataBoundListAdapter<WeatherViewData, WeatherItemBinding>(DiffCallback) {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): WeatherItemBinding {
        return WeatherItemBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: WeatherItemBinding, item: WeatherViewData) {
        binding.viewData = item
        // TalkBack
        binding.root.contentDescription = item.getTalkBackContentDescription()
    }
}

private val DiffCallback = object : DiffUtil.ItemCallback<WeatherViewData>() {

    override fun areItemsTheSame(oldItem: WeatherViewData, newItem: WeatherViewData): Boolean {
        return oldItem.cityId == newItem.cityId && oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: WeatherViewData, newItem: WeatherViewData): Boolean {
        return oldItem == newItem
    }
}
