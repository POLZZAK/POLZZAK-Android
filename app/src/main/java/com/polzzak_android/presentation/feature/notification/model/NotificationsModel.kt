package com.polzzak_android.presentation.feature.notification.model

//TODO API 모델 확인 후 수정 필요
data class NotificationsModel(
    val hasNextPage: Boolean = true,
    val nextOffset: Int = 0,
    val items: List<NotificationModel> = emptyList()
)
