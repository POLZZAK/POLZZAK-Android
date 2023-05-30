package com.polzzak_android.presentation.login.model

import com.polzzak_android.common.model.SocialLoginType
import com.polzzak_android.data.remote.model.response.LoginResponse

object LoginConvertor {
    fun LoginResponse.LoginResponseData?.toLoginInfoUiModel() =
        LoginInfoUiModel(
            userName = this?.userName,
            userType = this?.socialType?.toSocialLoginType(),
            accessToken = this?.accessToken ?: ""
        )

    fun String?.toSocialLoginType() = when (this) {
        "KAKAO" -> SocialLoginType.KAKAO
        "GOOGLE" -> SocialLoginType.GOOGLE
        else -> null
    }
}