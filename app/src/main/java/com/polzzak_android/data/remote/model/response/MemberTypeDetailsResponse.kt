package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MemberTypeDetailsResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: MemberTypesResponseData
) : BaseResponse<MemberTypeDetailsResponse.MemberTypesResponseData> {
    data class MemberTypesResponseData(
        val memberTypeDetailList: List<MemberTypeDetail>
    ) {
        data class MemberTypeDetail(
            @SerializedName("memberTypeDetailId")
            val memberTypeDetailId: Int,
            @SerializedName("detail")
            val detail: String
        )
    }
}
