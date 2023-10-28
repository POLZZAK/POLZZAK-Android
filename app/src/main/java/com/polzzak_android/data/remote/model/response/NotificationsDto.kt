package com.polzzak_android.data.remote.model.response

data class NotificationsDto(
    val response: NotificationsResponseDto?,
    val unreadNotificationCount: Int?
) {
    data class NotificationsResponseDto(
        val startId: Int?,
        val notificationDtoList: List<NotificationDto>,
    )
}