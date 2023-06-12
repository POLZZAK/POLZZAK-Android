package com.polzzak_android.data.remote.model.response

data class FamiliesResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: FamiliesDto?
) : BaseResponse<FamiliesDto>