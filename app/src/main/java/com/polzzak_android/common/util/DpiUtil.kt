package com.polzzak_android.common.util

import android.content.Context
import androidx.annotation.Px


fun convertDp2Px(context: Context, dp: Int): Int {
    val displayMetrics = context.resources
        .displayMetrics
    return (dp * displayMetrics.density + 0.5f).toInt()
}

fun convertPx2Dp(context: Context, @Px px: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return (px / displayMetrics.density + 0.5f).toInt()
}