package com.polzzak_android.presentation.component.bottomsheet.model

import com.polzzak_android.data.remote.model.response.UserInfoDto

data class SelectUserStampBoardModel(
    val userId: Int,
    val nickName: String,
    val userType: String? = null
)

fun UserInfoDto.toSelectUserStampBoardModel() = SelectUserStampBoardModel(
    userId = this.memberId,
    nickName = this.nickName,
    userType = this.memberType.detail
)