package com.polzzak_android.presentation.feature.notification.list.item

import android.content.Context
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.loadCircleImageUrl
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.notification.list.NotificationItemStateController
import com.polzzak_android.presentation.feature.notification.list.NotificationListClickListener
import com.polzzak_android.presentation.feature.notification.list.model.NotificationModel
import com.polzzak_android.presentation.feature.notification.list.model.NotificationStatusType
import com.polzzak_android.presentation.feature.notification.list.model.NotificationType

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
            tvTitle.text = model.title
            tvDate.text = model.date
            tvContent.text = createNotificationContentSpannable(
                context = binding.root.context,
                string = model.content
            )
            ivBtnRemoveNotification.setOnClickListener {
                clickListener.onClickDeleteNotification(id = model.id)
            }
            ivIconUnRead.isVisible = model.statusType == NotificationStatusType.UNREAD
            bindBtnLayout(binding = binding)
            bindProfile(binding = binding)
            bindHorizontalScroll(binding = binding)
            bindClickListener(binding = binding)
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
                        if (model.statusType == NotificationStatusType.REQUEST_FAMILY) 0 else
                            it.width + NOTIFICATION_REMOVE_LAYOUT_WIDTH_DP.toPx(context = binding.root.context)
                }

                hsvNotification.doOnPreDraw {
                    hsvNotification.scrollX =
                        if (model.statusType == NotificationStatusType.REQUEST_FAMILY) 0 else
                            itemStateController.getHorizontalScrollPosition(id = model.id)
                }
            }
            hsvNotification.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                if (model.statusType == NotificationStatusType.REQUEST_FAMILY) return@setOnScrollChangeListener
                clRemoveNotification.visibility = if (scrollX > 0) View.VISIBLE else View.INVISIBLE
                itemStateController.setHorizontalScrollPosition(id = model.id, position = scrollX)
            }
            clRemoveNotification.visibility =
                if (hsvNotification.scrollX == 0 || model.statusType == NotificationStatusType.REQUEST_FAMILY) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun bindBtnLayout(binding: ItemNotificationBinding) {
        with(binding) {
            clBtnLayout.isVisible = model.isButtonVisible
            listOf(tvBtnAccept, tvBtnReject).forEach {
                it.isVisible =
                    model.statusType != NotificationStatusType.REQUEST_FAMILY_REJECT && model.statusType != NotificationStatusType.REQUEST_FAMILY_ACCEPT
            }
            clBtnAccepted.isVisible =
                (model.statusType == NotificationStatusType.REQUEST_FAMILY_ACCEPT)
            clBtnRejected.isVisible =
                (model.statusType == NotificationStatusType.REQUEST_FAMILY_REJECT)
        }
    }

    private fun bindProfile(binding: ItemNotificationBinding) {
        with(binding) {
            clProfile.isVisible = (model.user != null)
            ivProfileImage.loadCircleImageUrl(imageUrl = model.user?.profileImageUrl)
            tvNickName.text = model.user?.nickName
        }
    }

    private fun bindClickListener(binding: ItemNotificationBinding) {
        if (model.type == NotificationType.FAMILY_REQUEST) {
            binding.tvBtnAccept.setOnClickListener {
                clickListener.onClickFamilyRequestAcceptClick(model = model)
            }
            binding.tvBtnReject.setOnClickListener {
                clickListener.onClickFamilyRequestRejectClick(model = model)
            }
        }
        binding.clNotification.setOnClickListener {
            model.link?.let {
                clickListener.onClickPageLink(it)
            }
        }
    }

    private fun createNotificationContentSpannable(context: Context, string: String) =
        SpannableBuilder.build(context = context) {
            var isOpen = false
            var startIdx = 0
            string.forEachIndexed { index, c ->
                if (!isOpen && c == '<') {
                    span(
                        string.substring(startIdx, index),
                        style = R.style.body_14_500,
                        textColor = R.color.gray_700
                    )
                    isOpen = true
                    startIdx = index + 3
                } else if (isOpen && c == '<') {
                    span(
                        string.substring(startIdx, index),
                        style = R.style.body_14_600,
                        textColor = R.color.gray_800
                    )
                    isOpen = false
                    startIdx = index + 4
                }
            }
            span(
                string.substring(startIdx, string.length), style = R.style.body_14_500,
                textColor = R.color.gray_700
            )
        }

    companion object {
        private const val NOTIFICATION_REMOVE_LAYOUT_WIDTH_DP = 56
    }
}