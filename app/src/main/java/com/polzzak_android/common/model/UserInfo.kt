package com.polzzak_android.common.model

data class UserInfo(
    val userName: String,
    val memberType: MemberType,
    val socialType: SocialLoginType,
    val nickName: String,
    val profileUrl: String?
)
