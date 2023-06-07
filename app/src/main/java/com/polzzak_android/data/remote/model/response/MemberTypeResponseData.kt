package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MemberTypeResponseData(
    @SerializedName("name")
    val name: String,
    @SerializedName("detail")
    val detail: String
)
