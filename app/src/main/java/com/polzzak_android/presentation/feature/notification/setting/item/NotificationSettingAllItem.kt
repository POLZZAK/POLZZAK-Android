package com.polzzak_android.presentation.feature.notification.setting.item

import com.polzzak_android.R
import com.polzzak_android.databinding.ItemNotificationSettingAllBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType

class NotificationSettingAllItem(val model: SettingMenuType.All) :
    BindableItem<ItemNotificationSettingAllBinding>() {
    override val layoutRes: Int = R.layout.item_notification_setting_all

    //TODO 비교함수 구현
    override fun areItemsTheSame(other: BindableItem<*>): Boolean = false

    override fun areContentsTheSame(other: BindableItem<*>): Boolean = false
    override fun bind(binding: ItemNotificationSettingAllBinding, position: Int) {
        binding.tvTitle.text = binding.root.context.getString(model.titleStringRes)
    }
}