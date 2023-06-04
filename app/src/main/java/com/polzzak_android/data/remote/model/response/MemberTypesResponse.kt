package com.polzzak_android.data.remote.model.response

data class MemberTypesResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: MemberTypesResponseData
) : BaseResponse<MemberTypesResponse.MemberTypesResponseData> {
    data class MemberTypesResponseData(
        val memberTypeDetailList: List<MemberTypeResponse>
    )
}
