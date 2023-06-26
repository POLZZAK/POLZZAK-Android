package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class FamiliesDto(
    @SerializedName("families")
    val families: List<UserInfoDto>
)
