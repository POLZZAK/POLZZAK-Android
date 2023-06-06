package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.polzzak_android.data.remote.model.RemoteMemberType

data class UserResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: UserResponseData?
) : BaseResponse<UserResponse.UserResponseData> {

    data class UserResponseData(
        @SerializedName("nickname")
        val nickName: String,
        @SerializedName("memberType")
        val memberType: RemoteMemberType,
        @SerializedName("profileUrl")
        val profileUrl: String
    )
}