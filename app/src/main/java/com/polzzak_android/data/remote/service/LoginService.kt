package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.LoginRequest
import com.polzzak_android.data.remote.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginService {

    @POST("/api/v1/auth/login/{loginType}")
    suspend fun requestLogin(
        @Path("loginType") loginType: String,
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}