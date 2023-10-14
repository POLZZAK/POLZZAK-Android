package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class PushTokenRequest(
    @SerializedName("token")
    val token: String
)
