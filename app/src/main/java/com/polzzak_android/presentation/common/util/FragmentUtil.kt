package com.polzzak_android.presentation.common.util

import androidx.fragment.app.Fragment
import com.polzzak_android.presentation.auth.login.sociallogin.SocialLoginManager

fun Fragment.getSocialLoginManager(): SocialLoginManager? = activity as? SocialLoginManager