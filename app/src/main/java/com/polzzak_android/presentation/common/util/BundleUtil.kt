package com.polzzak_android.presentation.common.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import timber.log.Timber

fun <T : Parcelable> Bundle.getParcelableOrNull(key: String, clazz: Class<T>): T? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, clazz)
        } else {
            @Suppress("DEPRECATION")
            getParcelable(key)
        }
    } catch (e: Exception) {
        Timber.e("getParcelableOrNull(key = $key, clazz : $clazz) error $e")
        null
    }
}

fun <T : Parcelable> Bundle.getParcelableArrayListOrNull(
    key: String,
    clazz: Class<T>
): ArrayList<T>? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableArrayList(key, clazz)
        } else {
            @Suppress("DEPRECATION")
            getParcelableArrayList(key)
        }
    } catch (e: Exception) {
        Timber.e("getParcelableArrayListOrNull(key = $key, clazz : $clazz) error $e")
        null
    }
}