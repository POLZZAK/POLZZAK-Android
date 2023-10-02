package com.polzzak_android.data.remote.model.response

data class RankingResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: RankingDto?
) : BaseResponse<RankingDto>
