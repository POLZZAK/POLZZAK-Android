package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.LinkRequest
import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import com.polzzak_android.data.remote.model.response.UserResponse
import com.polzzak_android.data.remote.model.response.FamiliesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FamilyService {
    //연동할 사용자 닉네임 조회
    @GET("/api/v1/families/users")
    suspend fun requestUserWithNickName(
        @Header("Authorization") authorization: String,
        @Query("nickname") nickName: String
    ): Response<UserResponse>

    //연동 신청
    @POST("/api/v1/families")
    suspend fun requestLink(
        @Header("Authorization") authorization: String,
        @Body linkRequest: LinkRequest
    ): Response<EmptyDataResponse>

    //연동 승인
    @PATCH("/api/v1/families/approve/{id}")
    suspend fun requestApproveLink(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<EmptyDataResponse>

    //연동 거절
    @DELETE("/api/v1/families/reject/{id}")
    suspend fun requestRejectLink(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<EmptyDataResponse>

    //연동 취소
    @DELETE("/api/v1/families/cancel/{id}")
    suspend fun requestDeleteLink(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<EmptyDataResponse>

    //나와 연동된 사용자
    @GET("/api/v1/families")
    suspend fun requestLinkedUsers(
        @Header("Authorization") authorization: String,
    ): Response<FamiliesResponse>

    //내가 요청한 사용자
    @GET("/api/v1/families/requests/sent")
    suspend fun requestSentRequestLinkUsers(
        @Header("Authorization") authorization: String,
    ): Response<FamiliesResponse>

    //나에게 요청한 사용자
    @GET("/api/v1/families/requests/received")
    suspend fun requestReceivedRequestLinkUsers(
        @Header("Authorization") authorization: String,
    ): Response<FamiliesResponse>

}