package com.polzzak_android.presentation.common.model

data class CommonButtonModel(
    val buttonCount: ButtonCount,
    val negativeButtonText: String? = "취소",
    val positiveButtonText: String? = "확인",
)

enum class ButtonCount {
    ZERO, ONE, TWO
}