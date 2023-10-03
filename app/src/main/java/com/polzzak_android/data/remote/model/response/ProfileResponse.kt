package com.polzzak_android.data.remote.model.response

data class ProfileResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: ProfileDto?
) : BaseResponse<ProfileDto>