package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class MakeStampRequest(
    @SerializedName("missionRequestId")
    val missionRequestId: Int,
    @SerializedName("missionId")
    val missionId: Int,
    @SerializedName("stampDesignId")
    val stampDesignId: Int
)
