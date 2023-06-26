package com.polzzak_android.data.remote.model.response

data class UserResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: UserInfoDto?
) : BaseResponse<UserInfoDto>