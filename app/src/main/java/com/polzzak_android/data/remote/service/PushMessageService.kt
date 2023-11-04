package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.request.PushTokenRequest
import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PushMessageService {
    @POST("/api/v1/push-token")
    suspend fun requestPostPushToken(
        @Header("Authorization") authorization: String,
        @Body pushTokenRequest: PushTokenRequest
    ): Response<EmptyDataResponse>
}