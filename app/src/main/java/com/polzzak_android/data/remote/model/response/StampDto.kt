package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class StampDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("stampDesignId")
    val stampDesignId: Int,
    @SerializedName("missionContent")
    val missionContent: String,
    @SerializedName("createdDate")
    val createdDate: String
)