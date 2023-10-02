package com.polzzak_android.presentation.feature.notification.list.model

sealed interface NotificationLinkType {
    object Home : NotificationLinkType
    object My : NotificationLinkType
    class StampDetail(val id: Int) : NotificationLinkType
    class CouponDetail(val id: Int) : NotificationLinkType
}