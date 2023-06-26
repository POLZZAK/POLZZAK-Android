package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class UserInfoDto(
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("nickname")
    val nickName: String,
    @SerializedName("memberType")
    val memberType: MemberTypeResponseData,
    @SerializedName("profileUrl")
    val profileUrl: String,
    @SerializedName("familyStatus")
    val status: String?
)