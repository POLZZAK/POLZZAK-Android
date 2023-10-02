package com.polzzak_android.data.remote.service

import com.polzzak_android.data.remote.model.response.EmptyDataResponse
import com.polzzak_android.data.remote.model.response.NotificationSettingsResponse
import com.polzzak_android.data.remote.model.response.NotificationsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationService {
    @GET("/api/v1/notifications")
    suspend fun requestNotifications(
        @Header("Authorization") authorization: String,
        @Query("startId") startId: Int?
    ): Response<NotificationsResponse>

    @DELETE("/api/v1/notifications")
    suspend fun deleteNotifications(
        @Header("Authorization") authorization: String,
        @Query("notificationIds") notificationIds: List<Int>
    ): Response<EmptyDataResponse>

    @GET("/api/v1/notifications/settings")
    suspend fun requestNotificationSettings(
        @Header("Authorization") authorization: String
    ): Response<NotificationSettingsResponse>

    @PATCH("/api/v1/notifications/settings")
    suspend fun requestSwitchNotificationSettings(
        @Header("Authorization") authorization: String,
        @Body settings: Map<String, Boolean>
    ): Response<EmptyDataResponse>
}