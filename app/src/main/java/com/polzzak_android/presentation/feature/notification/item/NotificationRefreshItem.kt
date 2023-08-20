package com.polzzak_android.presentation.feature.notification.item

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationRefreshBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.notification.model.NotificationRefreshStatusType

class NotificationRefreshItem(private val statusType: NotificationRefreshStatusType) :
    BindableItem<ItemNotificationRefreshBinding>() {
    override val layoutRes: Int = R.layout.item_notification_refresh

    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationRefreshItem && this.statusType == other.statusType

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationRefreshItem && this.statusType == other.statusType

    override fun bind(binding: ItemNotificationRefreshBinding, position: Int) {
        binding.ivRefresh.isVisible = (statusType != NotificationRefreshStatusType.Disable)
        statusType.progressDrawableRes?.let {
            val progressDrawable = ContextCompat.getDrawable(
                binding.root.context,
                it
            )
            binding.ivRefresh.setImageDrawable(progressDrawable)
            if (statusType == NotificationRefreshStatusType.Loading) {
                val rotateAnimation = RotateAnimation(
                    0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                ).apply {
                    duration = 1000
                    interpolator = LinearInterpolator()
                    repeatCount = Animation.INFINITE
                }
                binding.ivRefresh.startAnimation(rotateAnimation)
            }
        }
    }
}