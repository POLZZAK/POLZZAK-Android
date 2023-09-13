package com.polzzak_android.presentation.feature.notification.list.model

import android.text.Spannable
import androidx.annotation.StringRes
import androidx.core.text.toSpannable
import com.polzzak_android.R
import com.polzzak_android.common.util.safeLet
import com.polzzak_android.data.remote.model.response.NotificationDto
import com.polzzak_android.presentation.common.util.toLocalDateTimeOrNull
import com.polzzak_android.presentation.common.util.toNotificationDateString

data class NotificationModel(
    val id: Int,
    val date: String,
    val content: Spannable,
    val isButtonVisible: Boolean,
    val user: NotificationUserModel?,
    val statusType: NotificationStatusType,
    @get:StringRes
    val emojiStringRes: Int,
    @get:StringRes
    val titleStringRes: Int,
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
        date = this.createdDate?.toLocalDateTimeOrNull()?.toNotificationDateString() ?: return null,
        content = this.message?.toSpannable() ?: return null,
        isButtonVisible = (this.type == "FAMILY_REQUEST"),
        user = safeLet(this.sender?.id, this.sender?.nickName) { id, nickName ->
            NotificationModel.NotificationUserModel(
                userId = id,
                profileImageUrl = this.sender?.profileUrl,
                nickName = nickName
            )
        },
        statusType = this.status.toNotificationStatusType(),
        emojiStringRes = this.getEmojiStringRes() ?: return null,
        titleStringRes = this.getTitleStringRes() ?: return null,
    )
}

private fun NotificationDto.getEmojiStringRes() = when (this.type) {
    "FAMILY_REQUEST" -> R.string.notification_family_request_emoji
    "FAMILY_REQUEST_COMPLETE" -> R.string.notification_family_request_complete_emoji
    "LEVEL_UP" -> R.string.notification_level_up_emoji
    "LEVEL_DOWN" -> R.string.notification_level_down_emoji
    "STAMP_REQUEST" -> R.string.notification_stamp_request_emoji
    "REWARD_REQUEST" -> R.string.notification_reward_request_emoji
    "STAMP_BOARD_COMPLETE" -> R.string.notification_stamp_board_complete_emoji
    "REWARDED" -> R.string.notification_rewarded_emoji
    "REWARD_REQUEST_AGAIN" -> R.string.notification_reward_request_emoji
    "REWARD_FAIL" -> R.string.notification_reward_fail_emoji
    "CREATED_STAMP_BOARD" -> R.string.notification_created_stamp_board_emoji
    "ISSUED_COUPON" -> R.string.notification_issued_coupon_emoji
    "REWARDED_REQUEST" -> R.string.notification_rewarded_request_emoji
    else -> null
}

private fun NotificationDto.getTitleStringRes() = when (this.type) {
    "FAMILY_REQUEST" -> R.string.notification_family_request_title
    "FAMILY_REQUEST_COMPLETE" -> R.string.notification_family_request_complete_title
    "LEVEL_UP" -> R.string.notification_level_up_title
    "LEVEL_DOWN" -> R.string.notification_level_down_title
    "STAMP_REQUEST" -> R.string.notification_stamp_request_title
    "REWARD_REQUEST" -> R.string.notification_reward_request_title
    "STAMP_BOARD_COMPLETE" -> R.string.notification_stamp_board_complete_title
    "REWARDED" -> R.string.notification_rewarded_title
    "REWARD_REQUEST_AGAIN" -> R.string.notification_reward_request_title
    "REWARD_FAIL" -> R.string.notification_reward_fail_title
    "CREATED_STAMP_BOARD" -> R.string.notification_created_stamp_board_title
    "ISSUED_COUPON" -> R.string.notification_issued_coupon_title
    "REWARDED_REQUEST" -> R.string.notification_rewarded_request_title
    else -> null
}

private fun String?.toNotificationStatusType() = when (this) {
    "READ" -> NotificationStatusType.READ
    "UNREAD" -> NotificationStatusType.UNREAD
    "REQUEST_FAMILY" -> NotificationStatusType.REQUEST_FAMILY
    "REQUEST_FAMILY_ACCEPT" -> NotificationStatusType.REQUEST_FAMILY_ACCEPT
    "REQUEST_FAMILY_REJECT" -> NotificationStatusType.REQUEST_FAMILY_REJECT
    else -> NotificationStatusType.UNKNOWN
}