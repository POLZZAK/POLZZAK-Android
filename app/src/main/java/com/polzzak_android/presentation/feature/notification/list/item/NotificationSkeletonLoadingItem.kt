package com.polzzak_android.presentation.feature.notification.list.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationLoadingSkeletonBinding
import com.polzzak_android.presentation.common.util.BindableItem

class NotificationSkeletonLoadingItem : BindableItem<ItemNotificationLoadingSkeletonBinding>() {
    override val layoutRes: Int = R.layout.item_notification_loading_skeleton
    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationSkeletonLoadingItem

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationSkeletonLoadingItem

    override fun bind(binding: ItemNotificationLoadingSkeletonBinding, position: Int) {
        //do nothing
    }
}