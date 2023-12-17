package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class IssueCouponRequest(
    @SerializedName("rewardDate")
    val rewardDate: Long
)
