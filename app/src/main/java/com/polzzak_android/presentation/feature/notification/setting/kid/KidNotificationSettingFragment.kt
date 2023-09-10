package com.polzzak_android.presentation.feature.notification.setting.kid

import com.polzzak_android.presentation.feature.notification.setting.base.BaseNotificationSettingFragment
import com.polzzak_android.presentation.feature.notification.setting.model.SettingMenuType

class KidNotificationSettingFragment : BaseNotificationSettingFragment() {
    override val menuTypes: List<SettingMenuType> = listOf(
        SettingMenuType.Link,
        SettingMenuType.Level,
        SettingMenuType.NewStampBoard,
        SettingMenuType.PaymentCoupon,
        SettingMenuType.CheckDeliveryGift
    )
}