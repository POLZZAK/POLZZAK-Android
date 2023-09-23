package com.polzzak_android.presentation.feature.notification.setting.model

import androidx.annotation.StringRes
import com.polzzak_android.R

//TODO 순서 재배치
enum class SettingMenuType(
    val dataString: String,
    @StringRes val titleStringRes: Int,
    @StringRes val contentStringRes: Int
) {
    //TODO 선물 수령 알림 추가
    FAMILY_REQUEST(
        dataString = "familyRequest",
        titleStringRes = R.string.notification_setting_family_request_title,
        contentStringRes = R.string.notification_setting_family_request_content
    ),
    LEVEL(
        dataString = "level", titleStringRes = R.string.notification_setting_level_title,
        contentStringRes = R.string.notification_setting_level_content
    ),
    STAMP_REQUEST(
        dataString = "stampRequest",
        titleStringRes = R.string.notification_setting_stamp_request_title,
        contentStringRes = R.string.notification_setting_stamp_request_content
    ),
    STAMP_BOARD_COMPLETE(
        dataString = "stampBoardComplete",
        titleStringRes = R.string.notification_setting_stamp_board_complete_title,
        contentStringRes = R.string.notification_setting_stamp_board_complete_content
    ),
    REWARD_REQUEST(
        dataString = "rewardRequest",
        titleStringRes = R.string.notification_setting_reward_request_title,
        contentStringRes = R.string.notification_setting_reward_request_content
    ),
    REWARDED(
        dataString = "rewarded",
        titleStringRes = R.string.notification_setting_rewarded_title,
        contentStringRes = R.string.notification_setting_rewarded_content
    ),
    REWARD_FAIL(
        dataString = "rewardFail",
        titleStringRes = R.string.notification_setting_reward_fail_title,
        contentStringRes = R.string.notification_setting_reward_fail_content
    ),
    CREATED_STAMP_BOARD(
        dataString = "createdStampBoard",
        titleStringRes = R.string.notification_setting_created_stamp_board_title,
        contentStringRes = R.string.notification_setting_created_stamp_board_content
    ),
    ISSUED_COUPON(
        dataString = "issuedCoupon",
        titleStringRes = R.string.notification_setting_issued_coupon_title,
        contentStringRes = R.string.notification_setting_issued_coupon_content
    )
}
