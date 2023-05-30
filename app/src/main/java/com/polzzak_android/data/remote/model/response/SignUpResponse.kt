package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: SignUpResponseData?
) : BaseResponse<SignUpResponse.SignUpResponseData> {

    data class SignUpResponseData(
        @SerializedName("accessToken")
        val accessToken: String
    )
}
