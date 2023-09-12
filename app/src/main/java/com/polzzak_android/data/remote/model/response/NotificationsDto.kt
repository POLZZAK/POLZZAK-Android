package com.polzzak_android.data.remote.model.response

data class NotificationsDto(
    val startId: Int?,
    val notificationDtoList: List<NotificationDto>
)