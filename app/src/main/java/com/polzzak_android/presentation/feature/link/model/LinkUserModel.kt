package com.polzzak_android.presentation.feature.link.model

import com.polzzak_android.data.remote.model.response.UserInfoDto

data class LinkUserModel(
    val userId: Int,
    val nickName: String,
    val profileUrl: String?
)

fun UserInfoDto.toLinkUserModel() = LinkUserModel(
    userId = this.memberId,
    nickName = this.nickName,
    profileUrl = this.profileUrl
)