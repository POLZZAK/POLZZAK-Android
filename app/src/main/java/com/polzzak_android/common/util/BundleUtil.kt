package com.polzzak_android.common.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.polzzak_android.presentation.auth.signup.SignUpFragment
import timber.log.Timber

fun <T : Parcelable> Bundle.getParcelableOrNull(key: String, clazz: Class<T>): T? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(SignUpFragment.ARGUMENT_SOCIAL_LOGIN_TYPE_KEY, clazz)
        } else {
            @Suppress("DEPRECATION")
            getParcelable(SignUpFragment.ARGUMENT_SOCIAL_LOGIN_TYPE_KEY) as? T
        }
    } catch (e: Exception) {
        Timber.e("getParcelableOrNull(key = $key, clazz : $clazz) error $e")
        null
    }
}