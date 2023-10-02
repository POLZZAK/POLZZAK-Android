package com.polzzak_android.data.remote.model.response

data class CouponDetailResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: CouponDetailDto?
) : BaseResponse<CouponDetailDto>
