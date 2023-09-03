package com.polzzak_android.presentation.feature.myPage.notice.model

import java.time.LocalDate

data class MyNoticeModel(
    val id: Int,
    val title: String,
    val date: LocalDate,
    val content: String
)
