package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.LinkRequest
import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import com.polzzak_android.data.remote.model.response.UserResponse
import com.polzzak_android.data.remote.model.response.FamiliesResponse
import com.polzzak_android.data.remote.model.response.LinkRequestStatusResponse
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

    //연동 요청
    @POST("/api/v1/families")
    suspend fun requestLinkRequest(
        @Header("Authorization") authorization: String,
        @Body linkRequest: LinkRequest
    ): Response<EmptyDataResponse>

    //연동 요청 승인
    @PATCH("/api/v1/families/approve/{id}")
    suspend fun requestApproveLinkRequest(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<EmptyDataResponse>

    //연동 요청 거절
    @DELETE("/api/v1/families/reject/{id}")
    suspend fun requestRejectLinkRequest(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<EmptyDataResponse>

    //연동 요청 취소
    @DELETE("/api/v1/families/cancel/{id}")
    suspend fun requestCancelLinkRequest(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<EmptyDataResponse>

    //연동 삭제
    @DELETE("/api/v1/families/{id}")
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
    suspend fun requestSentRequestLinks(
        @Header("Authorization") authorization: String,
    ): Response<FamiliesResponse>

    //나에게 요청한 사용자
    @GET("/api/v1/families/requests/received")
    suspend fun requestReceivedRequestLinks(
        @Header("Authorization") authorization: String,
    ): Response<FamiliesResponse>

    //새로운 요청 여부
    @GET("/api/v1/families/new-request-mark")
    suspend fun requestIsRequestUpdated(
        @Header("Authorization") authorization: String
    ): Response<LinkRequestStatusResponse>
}