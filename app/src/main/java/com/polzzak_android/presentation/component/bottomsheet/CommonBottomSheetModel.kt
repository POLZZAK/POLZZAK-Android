package com.polzzak_android.presentation.component.bottomsheet

import com.polzzak_android.presentation.common.model.CommonButtonModel

data class CommonBottomSheetModel(
    val type: BottomSheetType,
    val title: String,
    val subTitle: String? = null,
    val contentList: List<*>,
    val button: CommonButtonModel
)

/**
 * MISSION : 미션
 * PROFILE_IMAGE : 프로필 이미지형
 * SELECT_STAMP_BOARD : 도장판 조회
 */
enum class BottomSheetType {
    MISSION, PROFILE_IMAGE, SELECT_STAMP_BOARD
}