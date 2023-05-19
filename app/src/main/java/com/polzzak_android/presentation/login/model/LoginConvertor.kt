package com.polzzak_android.presentation.login.model

import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.data.remote.model.response.LoginResponse

object LoginConvertor {
    fun LoginResponse.LoginData?.toLoginInfoUiModel() =
        LoginInfoUiModel(
            userName = this?.userName,
            userType = this?.socialType?.toSocialLoginType(),
            accessToken = this?.accessToken ?: ""
        )

    fun String?.toSocialLoginType() = when (this) {
        "kakao" -> SocialLoginType.KAKAO
        "google" -> SocialLoginType.GOOGLE
        else -> null
    }
}