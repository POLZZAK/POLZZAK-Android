package com.polzzak_android.presentation.auth.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SocialLoginType : Parcelable {
    KAKAO,
    GOOGLE
}