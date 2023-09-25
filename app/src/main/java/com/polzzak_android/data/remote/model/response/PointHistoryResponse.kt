package com.polzzak_android.data.remote.model.response

data class PointHistoryResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: PointHistoryDto?
) : BaseResponse<PointHistoryDto>
