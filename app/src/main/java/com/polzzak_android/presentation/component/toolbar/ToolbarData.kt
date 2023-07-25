package com.polzzak_android.presentation.component.toolbar

import androidx.navigation.NavController

/**
 * 툴바 세팅에 필요한 데이터
 * 필요한 값만 세팅.
 */
data class ToolbarData(
    val popStack: NavController? = null,                // 시스템 백버튼 필요한 경우 필요
    val titleText: String? = null,                      // 타이틀 텍스트
    val iconText: String? = null,                       // 아이콘 텍스트
    val iconImageId: Int? = null,                       // 아이콘 이미지 리소스 id
    val iconInteraction: ToolbarIconInteraction? = null // 아이콘 인터랙션 작성하는 interface
)