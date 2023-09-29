package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MemberPointResponseData(
    @SerializedName("point") val point: Int,
    @SerializedName("level") val level: Int
)
