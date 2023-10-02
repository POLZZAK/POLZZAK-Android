package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class ReceiveCouponRequest(
    @SerializedName("stampBoardId")
    val stampBoardId: Int
)
