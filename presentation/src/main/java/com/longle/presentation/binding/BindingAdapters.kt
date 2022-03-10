package com.longle.presentation.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.longle.presentation.ui.common.DataBoundListAdapter

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("errorText")
fun errorText(view: TextInputLayout, text: String?) {
    view.error = text
}

@BindingAdapter("data")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    if (recyclerView.adapter is DataBoundListAdapter<*, *>) {
        (recyclerView.adapter as DataBoundListAdapter<T, *>).submitList(data)
    }
}
