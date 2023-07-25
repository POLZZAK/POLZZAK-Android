package com.polzzak_android.presentation.common.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.polzzak_android.presentation.feature.auth.login.sociallogin.SocialLoginManager
import com.polzzak_android.presentation.feature.root.MainActivity

fun Fragment.getSocialLoginManager(): SocialLoginManager? = activity as? SocialLoginManager

fun Fragment.getAccessTokenOrNull(): String? = (activity as? MainActivity)?.getAccessToken()

fun Fragment.shotBackPressed() {
    (activity as? MainActivity)?.backPressed()
}

fun Fragment.hideKeyboard() {
    activity?.run {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { currentFocus ->
            inputManager?.hideSoftInputFromWindow(
                currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}