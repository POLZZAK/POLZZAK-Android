package com.polzzak_android.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class GoogleOAuthRequest(
    @SerializedName("grant_type")
    val grantType: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("access_type")
    val accessType: String,
    @SerializedName("code")
    val code: String
)
