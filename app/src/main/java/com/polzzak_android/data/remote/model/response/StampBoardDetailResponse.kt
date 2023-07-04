package com.polzzak_android.data.remote.model.response

data class StampBoardDetailResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: StampBoardDetailDto?
) : BaseResponse<StampBoardDetailDto>
