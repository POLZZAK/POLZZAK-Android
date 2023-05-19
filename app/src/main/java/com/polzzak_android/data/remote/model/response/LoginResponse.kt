package com.polzzak_android.data.remote.model.response

data class LoginResponse(
    override val code: Int,
    override val message: List<String>?,
    override val data: LoginData
) : BaseResponse<LoginResponse.LoginData> {

    data class LoginData(
        val accessToken: String?,
        val userName: String?,
        val socialType: String?,
    )
}
