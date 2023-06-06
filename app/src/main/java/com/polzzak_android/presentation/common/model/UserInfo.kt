package com.polzzak_android.presentation.common.model

import com.polzzak_android.presentation.auth.signup.model.MemberTypeDetail

data class UserInfo(
    val memberType: MemberTypeDetail? = null,
    val nickName: String? = null,
    val profileUrl: String? = null
)
