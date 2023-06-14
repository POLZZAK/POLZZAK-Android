package com.polzzak_android.common.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

fun getDeviceSize(context: Context): Pair<Int, Int> {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        windowManager.currentWindowMetrics.run {
            return Pair(bounds.width(), bounds.height())
        }
    } else {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        return Pair(width, height)
    }
}