package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class CouponDetailDto(
    @SerializedName("couponId")
    val couponId: Int,
    @SerializedName("reward")
    val reward: String,
    @SerializedName("guardian")
    val guardian: ProfileDto,
    @SerializedName("kid")
    val kid: ProfileDto,
    @SerializedName("missionContents")
    val missionContents: List<String>,
    @SerializedName("stampCount")
    val stampCount: Int,
    @SerializedName("state")
    val state: String,
    @SerializedName("rewardDate")
    val rewardDate: String,     // 상품 수령 예정 날짜
    @SerializedName("rewardRequestDate")
    val rewardRequestDate: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String
) {
    data class ProfileDto(
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("profileUrl")
        val profileUrl: String
    )
}
