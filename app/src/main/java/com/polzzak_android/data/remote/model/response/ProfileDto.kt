package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("nickname")
    val nickName: String,
    @SerializedName("memberPoint")
    val memberPoint: MemberPointResponseData,
    @SerializedName("memberType")
    val memberType: MemberTypeResponseData,
    @SerializedName("profileUrl")
    val profileUrl: String,
    @SerializedName("familyCount")
    val linkedUser: Int
)