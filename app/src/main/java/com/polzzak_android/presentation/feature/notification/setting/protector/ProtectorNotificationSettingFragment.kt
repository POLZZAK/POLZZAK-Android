package com.polzzak_android.presentation.feature.notification.setting.protector

import com.polzzak_android.presentation.feature.notification.setting.base.BaseNotificationSettingFragment
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType

class ProtectorNotificationSettingFragment : BaseNotificationSettingFragment() {
    override val menuTypes: List<SettingMenuType> = listOf(
        SettingMenuType.Link,
        SettingMenuType.Level,
        SettingMenuType.RequestStamp,
        SettingMenuType.RequestGift,
        SettingMenuType.CompleteStampBoard,
        SettingMenuType.ReceiveGift,
        SettingMenuType.BreakPromise
    )
}