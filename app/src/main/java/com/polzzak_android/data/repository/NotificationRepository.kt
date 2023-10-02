package com.polzzak_android.data.repository

import com.polzzak_android.data.remote.service.NotificationService
import com.polzzak_android.data.remote.util.createHeaderAuthorization
import com.polzzak_android.data.remote.util.requestCatching
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationService: NotificationService) {
    suspend fun requestNotifications(
        accessToken: String,
        startId: Int?
    ) = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        notificationService.requestNotifications(
            authorization = authorization,
            startId = startId
        )
    }

    suspend fun deleteNotifications(
        accessToken: String,
        notificationIds: List<Int>
    ) = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        notificationService.deleteNotifications(
            authorization = authorization,
            notificationIds = notificationIds
        )
    }

    suspend fun requestNotificationSettings(
        accessToken: String
    ) = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        notificationService.requestNotificationSettings(
            authorization = authorization
        )
    }

    suspend fun requestSwitchNotificationSettings(
        accessToken: String,
        settingsMap: Map<String, Boolean>
    ) = requestCatching {
        val authorization = createHeaderAuthorization(accessToken = accessToken)
        notificationService.requestSwitchNotificationSettings(
            authorization = authorization,
            settings = settingsMap
        )
    }
}