package com.polzzak_android.data.remote.model.response

data class LoginResponse(
    override val code: Int,
    override val message: List<String>?,
    override val data: LoginResponseData?
) : BaseResponse<LoginResponse.LoginResponseData> {

    data class LoginResponseData(
        val accessToken: String?,
        val userName: String?,
        val socialType: String?,
    )
}
