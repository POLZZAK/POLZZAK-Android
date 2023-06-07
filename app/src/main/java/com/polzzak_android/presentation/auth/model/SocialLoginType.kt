package com.polzzak_android.presentation.auth.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SocialLoginType : Parcelable {
    KAKAO,
    GOOGLE
}

fun asSocialLoginTypeOrNull(type: String): SocialLoginType? {
    return when (type.lowercase()) {
        "kakao" -> SocialLoginType.KAKAO
        "google" -> SocialLoginType.GOOGLE
        else -> null
    }
}