package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class MissionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("content")
    val content: String
)