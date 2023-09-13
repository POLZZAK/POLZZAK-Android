package com.polzzak_android.presentation.feature.notification.list.model

data class NotificationsModel(
    val nextId: Int? = null,
    val items: List<NotificationModel>? = null,
    val refreshStatusType: NotificationRefreshStatusType = NotificationRefreshStatusType.Disable
)
