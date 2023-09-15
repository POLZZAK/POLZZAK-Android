package com.polzzak_android.presentation.feature.notification.list.model

import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.response.NotificationDto
import com.polzzak_android.presentation.common.util.toLocalDateTimeOrNull
import com.polzzak_android.presentation.common.util.toNotificationDateString

data class NotificationModel(
    val id: Int,
    val title: String,
    val date: String,
    val content: String,
    val isButtonVisible: Boolean,
    val user: NotificationUserModel?,
    val statusType: NotificationStatusType,
    val type: NotificationType,
    val link: NotificationLinkType?
) {
    data class NotificationUserModel(
        val userId: Int,
        val profileImageUrl: String?,
        val nickName: String
    )
}

fun NotificationDto.toNotificationModel(): NotificationModel? {
    return NotificationModel(
        id = this.id ?: return null,
        title = this.title.orEmpty(),
        date = this.createdDate?.toLocalDateTimeOrNull()?.toNotificationDateString() ?: return null,
        content = this.message.orEmpty(),
        isButtonVisible = (this.type == "FAMILY_REQUEST"),
        user = safeLet(this.sender?.id, this.sender?.nickName) { id, nickName ->
            NotificationModel.NotificationUserModel(
                userId = id,
                profileImageUrl = this.sender?.profileUrl,
                nickName = nickName
            )
        },
        statusType = this.status.toNotificationStatusType(),
        type = NotificationType.values()
            .find { this.type?.compareTo(it.name, ignoreCase = true) == 0 } ?: return null,
        link = this.link?.toNotificationLinkType()
    )
}

private fun String?.toNotificationStatusType() = when (this) {
    "READ" -> NotificationStatusType.READ
    "UNREAD" -> NotificationStatusType.UNREAD
    "REQUEST_FAMILY" -> NotificationStatusType.REQUEST_FAMILY
    "REQUEST_FAMILY_ACCEPT" -> NotificationStatusType.REQUEST_FAMILY_ACCEPT
    "REQUEST_FAMILY_REJECT" -> NotificationStatusType.REQUEST_FAMILY_REJECT
    else -> NotificationStatusType.UNKNOWN
}

private fun String.toNotificationLinkType(): NotificationLinkType? {
    val destination = this.substringBefore('/')
    val id = this.substringAfter('/').toIntOrNull()
    return when (destination) {
        "home" -> NotificationLinkType.Home
        "my-page" -> NotificationLinkType.My
        "stamp-board" -> NotificationLinkType.StampDetail(id = id ?: return null)
        "coupon" -> NotificationLinkType.CouponDetail(id = id ?: return null)
        else -> null
    }
}