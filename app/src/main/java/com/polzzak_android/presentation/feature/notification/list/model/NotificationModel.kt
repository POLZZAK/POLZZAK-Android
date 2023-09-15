package com.polzzak_android.presentation.feature.notification.list.model

import android.text.Spannable
import androidx.core.text.toSpannable
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