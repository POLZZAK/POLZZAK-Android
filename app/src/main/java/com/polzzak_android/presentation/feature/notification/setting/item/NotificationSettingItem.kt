package com.polzzak_android.presentation.feature.notification.setting.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationSettingBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType

class NotificationSettingItem(val model: SettingMenuType.Menu) :
    BindableItem<ItemNotificationSettingBinding>() {
    override val layoutRes: Int = R.layout.item_notification_setting

    //TODO 비교함수 구현
    override fun areItemsTheSame(other: BindableItem<*>): Boolean = false

    override fun areContentsTheSame(other: BindableItem<*>): Boolean = false

    override fun bind(binding: ItemNotificationSettingBinding, position: Int) {
        with(binding) {
            val context = root.context
            tvTitle.text = context.getString(model.titleStringRes)
            tvContent.text = context.getString(model.contentStringRes)
        }
    }
}