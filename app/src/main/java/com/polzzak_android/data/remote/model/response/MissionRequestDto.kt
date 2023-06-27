package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MissionRequestDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("missionContent")
    val missionContent: String,
    @SerializedName("createdDate")
    val createdDate: String
)