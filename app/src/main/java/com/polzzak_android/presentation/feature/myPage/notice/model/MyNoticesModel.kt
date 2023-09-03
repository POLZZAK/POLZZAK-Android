package com.polzzak_android.presentation.feature.myPage.notice.model

data class MyNoticesModel(
    val notices: List<MyNoticeModel> = emptyList(),
    val nextId: Int? = null,
    val hasNextPage: Boolean = true
)