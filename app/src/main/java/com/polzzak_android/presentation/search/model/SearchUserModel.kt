package com.polzzak_android.presentation.search.model

import com.polzzak_android.presentation.common.model.MemberType

data class SearchUserModel(
    val userId: Int,
    val nickName: String,
    val memberType: MemberType,
    val profileUrl: String
)
