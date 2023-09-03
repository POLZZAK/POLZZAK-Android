package com.polzzak_android.presentation.component.dialog

/**
 * 다이얼로그 캘린더형에서 선택한 날짜의 반환값
 */
data class SelectedDateModel(
    val year: Int,
    val month: Int,
    val day: Int
)