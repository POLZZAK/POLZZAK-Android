package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class StampRequest(
    @SerializedName("stampBoardId")
    val stampBoardId: Int,
    @SerializedName("missionId")
    val missionId: Int,
    @SerializedName("guardianId")
    val guardianId: Int
)
