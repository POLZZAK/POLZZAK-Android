package com.polzzak_android.presentation.feature.myPage.model

data class FAQModel(
    val type: FAQType,
    val title: String,
    val content: String
)

enum class FAQType {
    STAMP, COUPON, ACCOUNT, POINT, LINKED
}
