package com.polzzak_android.presentation.feature.notification.model

import android.text.Spannable
import androidx.annotation.StringRes
import com.polzzak_android.R

//TODO api response 확인 후 알림타입추가
sealed interface NotificationModel {
    @get:StringRes
    val emojiStringRes: Int

    @get:StringRes
    val titleStringRes: Int
    val date: String
    val content: Spannable
    val isButtonVisible: Boolean
    val userInfo: NotificationUserModel?

    data class NotificationUserModel(
        val profileImageUrl: String,
        val nickName: String
    )

    class RequestLink(
        override val date: String,
        override val content: Spannable,
        nickName: String,
        profileImageUrl: String,
    ) : NotificationModel {
        override val emojiStringRes: Int = R.string.notification_request_link_emoji
        override val titleStringRes: Int = R.string.notification_request_link_title
        override val isButtonVisible = true
        override val userInfo =
            NotificationUserModel(profileImageUrl = profileImageUrl, nickName = nickName)
    }

    class CompleteLink(
        override val date: String,
        override val content: Spannable,
        nickName: String,
        profileImageUrl: String,
    ) : NotificationModel {
        override val emojiStringRes: Int = R.string.notification_complete_link_emoji
        override val titleStringRes: Int = R.string.notification_complete_link_title
        override val isButtonVisible = false
        override val userInfo =
            NotificationUserModel(profileImageUrl = profileImageUrl, nickName = nickName)
    }

    class LevelUp(
        override val date: String,
        override val content: Spannable
    ) : NotificationModel {
        override val emojiStringRes: Int = R.string.notification_level_up_emoji
        override val titleStringRes: Int = R.string.notification_level_up_title
        override val isButtonVisible = false
        override val userInfo = null
    }

    class LevelDown(
        override val date: String,
        override val content: Spannable
    ) : NotificationModel {
        override val emojiStringRes: Int = R.string.notification_level_down_emoji
        override val titleStringRes: Int = R.string.notification_level_down_title
        override val isButtonVisible = false
        override val userInfo = null
    }
}
