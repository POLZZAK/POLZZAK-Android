package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.LoginRequest
import com.polzzak_android.data.remote.model.response.CheckNickNameValidationResponse
import com.polzzak_android.data.remote.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {
    @POST("/api/v1/auth/login/{loginType}")
    suspend fun requestLogin(
        @Path("loginType") loginType: String,
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/api/v1/auth/validate/nickname")
    suspend fun requestCheckNickNameValidation(
        @Query("value") nickName: String,
    ): Response<CheckNickNameValidationResponse>
}