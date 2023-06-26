package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class LinkRequest(
    @SerializedName("targetId")
    val targetId: Int
)
