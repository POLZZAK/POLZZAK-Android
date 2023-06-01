package com.polzzak_android.presentation.common.model

import com.polzzak_android.presentation.auth.model.SocialLoginType

data class UserInfo(
    val userName: String,
    val memberType: MemberType,
    val socialType: SocialLoginType,
    val nickName: String,
    val profileUrl: String?
)
