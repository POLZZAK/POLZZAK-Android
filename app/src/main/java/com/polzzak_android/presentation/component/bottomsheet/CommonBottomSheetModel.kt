package com.polzzak_android.presentation.component.bottomsheet

import android.text.Spannable
import com.polzzak_android.presentation.common.model.CommonButtonModel

data class CommonBottomSheetModel(
    val type: BottomSheetType,
    val title: Spannable,
    val subTitle: Spannable? = null,
    val contentList: List<*>,
    val button: CommonButtonModel
)

/**
 * MISSION : 미션
 * PROFILE_IMAGE : 프로필 이미지형
 * SELECT_STAMP_BOARD : 도장판 조회
 * COUPON : 쿠폰
 */
enum class BottomSheetType {
    MISSION, PROFILE_IMAGE, SELECT_STAMP_BOARD, COUPON
}