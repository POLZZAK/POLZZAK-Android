package com.polzzak_android.presentation.component.bottomsheet.model

import com.polzzak_android.data.remote.model.response.UserInfoDto

data class SelectUserMakeBoardModelModel(
    val userId: Int,
    val nickName: String,
    val profileUrl: String
)

fun UserInfoDto.toSelectUserMakeBoardModelModel() = SelectUserStampBoardModel(
    userId = this.memberId,
    nickName = this.nickName,
    userType = this.profileUrl
)