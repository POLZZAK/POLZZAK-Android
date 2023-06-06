package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MemberTypeDetailsResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: MemberTypeDetailsResponseData
) : BaseResponse<MemberTypeDetailsResponse.MemberTypeDetailsResponseData> {
    data class MemberTypeDetailsResponseData(
        val memberTypeDetailList: List<MemberTypeDetailResponseData>
    ) {
        data class MemberTypeDetailResponseData(
            @SerializedName("memberTypeDetailId")
            val memberTypeDetailId: Int,
            @SerializedName("detail")
            val detail: String
        )
    }
}
