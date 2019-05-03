package com.adammcneilly.graphqldemo.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityCondition")
fun View.visibilityCondition(condition: Boolean?) {
    this.visibility = if (condition == true) View.VISIBLE else View.GONE
}