package com.polzzak_android.presentation.common.util

import androidx.fragment.app.Fragment
import com.polzzak_android.presentation.auth.login.sociallogin.SocialLoginManager
import com.polzzak_android.presentation.common.MainActivity

fun Fragment.getSocialLoginManager(): SocialLoginManager? = activity as? SocialLoginManager

fun Fragment.getAccessTokenOrNull(): String? = (activity as? MainActivity)?.getAccessToken()

fun Fragment.shotBackPressed() {
    (activity as? MainActivity)?.backPressed()
}