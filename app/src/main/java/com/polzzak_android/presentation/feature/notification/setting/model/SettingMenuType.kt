package com.polzzak_android.presentation.feature.notification.setting.model

import androidx.annotation.StringRes
import com.polzzak_android.R

//TODO 서버타입과 이름 맞추기(현재 임의로 이름 지음)
sealed interface SettingMenuType {
    @get:StringRes
    val titleStringRes: Int

    @get:StringRes
    val contentStringRes: Int

    object Link : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_link_title
        override val contentStringRes: Int = R.string.notification_setting_link_content
    }

    object Level : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_level_title
        override val contentStringRes: Int = R.string.notification_setting_level_content
    }

    object RequestStamp : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_request_stamp_title
        override val contentStringRes: Int = R.string.notification_setting_request_stamp_content
    }

    object RequestGift : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_request_gift_title
        override val contentStringRes: Int = R.string.notification_setting_request_gift_content
    }

    object CompleteStampBoard : SettingMenuType {
        override val titleStringRes: Int =
            R.string.notification_setting_complete_stamp_board_title
        override val contentStringRes: Int =
            R.string.notification_setting_complete_stamp_board_content
    }

    object ReceiveGift : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_receive_gift_title
        override val contentStringRes: Int = R.string.notification_setting_receive_gift_content
    }

    object BreakPromise : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_break_promise_title
        override val contentStringRes: Int = R.string.notification_setting_break_promise_content
    }

    object NewStampBoard : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_new_stamp_board_title
        override val contentStringRes: Int =
            R.string.notification_setting_new_stamp_board_content
    }

    object PaymentCoupon : SettingMenuType {
        override val titleStringRes: Int = R.string.notification_setting_payment_coupon_title
        override val contentStringRes: Int =
            R.string.notification_setting_payment_coupon_content
    }

    object CheckDeliveryGift : SettingMenuType {
        override val titleStringRes: Int =
            R.string.notification_setting_check_delivery_gift_title
        override val contentStringRes: Int =
            R.string.notification_setting_check_delivery_gift_content
    }
}