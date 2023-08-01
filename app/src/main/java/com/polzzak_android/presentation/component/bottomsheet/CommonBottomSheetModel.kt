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
 * MISSION :
 * PROFILE_IMAGE : 프로필 이미지형
 * PROFILE_LIST : 프로필 기본형
 */
enum class BottomSheetType {
    MISSION, PROFILE_IMAGE, PROFILE_LIST
}