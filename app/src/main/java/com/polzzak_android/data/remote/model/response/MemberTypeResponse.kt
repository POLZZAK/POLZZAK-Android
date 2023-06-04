package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MemberTypeResponse(
    @SerializedName("memberTypeDetailId")
    val memberTypeDetailId: Int,
    @SerializedName("detail")
    val detail: String
)
