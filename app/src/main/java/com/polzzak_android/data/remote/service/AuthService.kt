package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.LoginRequest
import com.polzzak_android.data.remote.model.response.CheckNickNameValidationResponse
import com.polzzak_android.data.remote.model.response.LoginResponse
import com.polzzak_android.data.remote.model.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("/api/v1/auth/register")
    suspend fun requestSignUp(
        @Part("registerRequest") registerRequest: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<SignUpResponse>
}