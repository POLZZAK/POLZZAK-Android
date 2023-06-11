package com.polzzak_android.data.remote.model.response

data class UsersResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: List<UserInfoDto>?
) : BaseResponse<List<UserInfoDto>>