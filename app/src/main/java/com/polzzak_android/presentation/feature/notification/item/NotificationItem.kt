package com.polzzak_android.presentation.feature.notification.item

import androidx.core.view.isVisible
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.loadCircleImageUrl
import com.polzzak_android.presentation.feature.notification.model.NotificationModel

class NotificationItem(private val model: NotificationModel) :
    BindableItem<ItemNotificationBinding>() {
    override val layoutRes: Int = R.layout.item_notification
    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationItem && this.model.id == other.model.id

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationItem && this.model == other.model

    override fun bind(binding: ItemNotificationBinding, position: Int) {
        with(binding) {
            val context = root.context
            tvEmoji.text = context.getString(model.emojiStringRes)
            tvTitle.text = context.getString(model.titleStringRes)
            tvDate.text = model.date
            tvContent.text = model.content
            bindBtnLayout(binding = binding)
            bindProfile(binding = binding)
        }
    }

    private fun bindBtnLayout(binding: ItemNotificationBinding) {
        with(binding) {
            clBtnLayout.isVisible = model.isButtonVisible
            tvBtnAccept.setOnClickListener {
                //TODO 수락 버튼 클릭
            }
            tvBtnReject.setOnClickListener {
                //TODO 거절 버튼 클릭
            }
        }

    }

    private fun bindProfile(binding: ItemNotificationBinding) {
        with(binding) {
            clProfile.isVisible = (model.userInfo != null)
            ivProfileImage.loadCircleImageUrl(imageUrl = model.userInfo?.profileImageUrl)
            tvNickName.text = model.userInfo?.nickName
        }
    }

}