package com.polzzak_android.presentation.feature.notification.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationEmptyBinding
import com.polzzak_android.presentation.common.util.BindableItem

class NotificationEmptyItem : BindableItem<ItemNotificationEmptyBinding>() {
    override val layoutRes: Int = R.layout.item_notification_empty

    override fun areItemsTheSame(other: BindableItem<*>): Boolean = other is NotificationEmptyItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationEmptyItem

    override fun bind(binding: ItemNotificationEmptyBinding, position: Int) {
        //do nothing
    }
}