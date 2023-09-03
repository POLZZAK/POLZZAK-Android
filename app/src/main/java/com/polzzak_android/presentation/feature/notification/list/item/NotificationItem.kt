package com.polzzak_android.presentation.feature.notification.list.item

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.loadCircleImageUrl
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.notification.list.NotificationItemStateController
import com.polzzak_android.presentation.feature.notification.list.NotificationListClickListener
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel

//TODO 버튼 클릭 상태 적용
class NotificationItem(
    private val model: NotificationModel,
    private val itemStateController: NotificationItemStateController,
    private val clickListener: NotificationListClickListener
) : BindableItem<ItemNotificationBinding>() {
    override val layoutRes: Int = R.layout.item_notification
    override fun areItemsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationItem && this.model.id == other.model.id

    override fun areContentsTheSame(other: BindableItem<*>): Boolean =
        other is NotificationItem && this.model == other.model && !itemStateController.getIsRefreshedSuccess()

    override fun bind(binding: ItemNotificationBinding, position: Int) {
        with(binding) {
            val context = root.context
            tvEmoji.text = context.getString(model.emojiStringRes)
            tvTitle.text = context.getString(model.titleStringRes)
            tvDate.text = model.date
            tvContent.text = model.content
            ivBtnRemoveNotification.setOnClickListener {
                clickListener.onClickDeleteNotification(id = model.id)
            }
            bindBtnLayout(binding = binding)
            bindProfile(binding = binding)
            bindHorizontalScroll(binding = binding)
        }
    }

    private fun bindHorizontalScroll(binding: ItemNotificationBinding) {
        with(binding) {
            clNotification.doOnPreDraw {
                it.updateLayoutParams {
                    width = it.width
                }
                clRemoveNotification.updateLayoutParams {
                    width =
                        it.width + NOTIFICATION_REMOVE_LAYOUT_WIDTH_DP.toPx(context = binding.root.context)
                }
                hsvNotification.doOnPreDraw {
                    hsvNotification.scrollX =
                        itemStateController.getHorizontalScrollPosition(id = model.id)
                }
            }
            hsvNotification.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                clRemoveNotification.visibility = if (scrollX > 0) View.VISIBLE else View.INVISIBLE
                itemStateController.setHorizontalScrollPosition(id = model.id, position = scrollX)
            }
            clRemoveNotification.visibility =
                if (hsvNotification.scrollX == 0) View.INVISIBLE else View.VISIBLE
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

    companion object {
        private const val NOTIFICATION_REMOVE_LAYOUT_WIDTH_DP = 56
    }
}