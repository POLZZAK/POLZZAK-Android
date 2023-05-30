package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    override val code: Int,
    override val messages: List<String>?,
    override val data: LoginResponseData?
) : BaseResponse<LoginResponse.LoginResponseData> {

    data class LoginResponseData(
        @SerializedName("accessToken")
        val accessToken: String?,
        @SerializedName("username")
        val userName: String?,
        @SerializedName("socialType")
        val socialType: String?,
    )
}
