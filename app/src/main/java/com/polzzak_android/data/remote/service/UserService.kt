package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import com.polzzak_android.data.remote.model.response.ProfileResponse
import com.polzzak_android.data.remote.model.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path


interface UserService {
    // 사용자 정보 조회
    @GET("/api/v1/users/me")
    suspend fun requestUserInfo(
        @Header("Authorization") authorization: String,
    ): Response<UserResponse>

    // 사용자 프로필 조회
    @GET("/api/v1/users/me")
    suspend fun requestProfile(
        @Header("Authorization") authorization: String,
    ): Response<ProfileResponse>

    // 사용자 프로필 업데이트
    @Multipart
    @PATCH("/api/v1/users/profile")
    suspend fun updateUserInfo(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String,
        @Part("profile") profile: MultipartBody
    ): Response<EmptyDataResponse>

    // 사용자 닉네임 업데이트
    @PATCH("/api/v1/users/nickname/{nickname}")
    suspend fun updateUserNickname(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String,
        @Path("nickname") nickname: String
    ): Response<EmptyDataResponse>

    @DELETE("/api/v1/users")
    suspend fun deleteUser(
        @Header("Authorization") authorization: String,
    ): Response<EmptyDataResponse>
}