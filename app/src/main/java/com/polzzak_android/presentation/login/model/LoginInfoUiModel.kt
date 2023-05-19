package com.polzzak_android.presentation.login.model

import com.polzzak_android.common.model.SocialLoginType

data class LoginInfoUiModel(
    val userName: String?,
    val userType: SocialLoginType?,
    val accessToken: String?
)
