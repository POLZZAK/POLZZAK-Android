package com.polzzak_android.presentation.component.newbottomsheet.base

/**
 * 바텀시트의 화면 이동이나 닫기 이벤트에 사용할 enum
 */
enum class SheetEvent {
    NEXT,   // 다음 화면 이동
    PREV,   // 이전 화면 이동
    CLOSE,  // 바텀시트 닫기
    ACTION  // 다음, 이전, 닫기 이외의 동작을 해야할 때
}