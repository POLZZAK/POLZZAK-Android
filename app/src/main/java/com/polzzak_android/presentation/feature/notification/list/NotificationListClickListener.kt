package com.polzzak_android.presentation.feature.notification.list

import com.polzzak_android.presentation.feature.notification.list.model.NotificationLinkType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel

interface NotificationListClickListener {
    fun onClickDeleteNotification(id: Int)
    fun onClickFamilyRequestAcceptClick(model: NotificationModel)
    fun onClickFamilyRequestRejectClick(model: NotificationModel)
    fun onClickPageLink(type: NotificationLinkType)
}