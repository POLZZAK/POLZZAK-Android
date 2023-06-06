package com.polzzak_android.presentation.common.model

import com.polzzak_android.presentation.auth.model.MemberTypeDetail

@Deprecated("accessToken으로 페이지마다 필요한 정보 직접 획득")
data class UserInfo(
    val accessToken: String? = null,
    val memberId: Int? = null,
    val memberType: MemberTypeDetail? = null,
    val nickName: String? = null,
    val profileUrl: String? = null
)
