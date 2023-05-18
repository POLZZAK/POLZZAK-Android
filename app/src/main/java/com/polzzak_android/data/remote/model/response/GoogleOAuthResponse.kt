package com.polzzak_android.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class GoogleOAuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
)
