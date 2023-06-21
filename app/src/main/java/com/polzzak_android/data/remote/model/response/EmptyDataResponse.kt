package com.polzzak_android.data.remote.model.response

data class EmptyDataResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: Unit?
) : BaseResponse<Unit>