package com.polzzak_android.presentation.auth.login.model

import com.polzzak_android.presentation.auth.model.SocialLoginType

data class LoginInfoUiModel(
    val userName: String?,
    val userType: SocialLoginType?,
    val accessToken: String?
)
