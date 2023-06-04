package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface UserService {
    @GET("/api/v1/users/me")
    fun requestUserInfo(
        @Header("Authorization") accessToken: String,
    ): Response<UserResponse>
}