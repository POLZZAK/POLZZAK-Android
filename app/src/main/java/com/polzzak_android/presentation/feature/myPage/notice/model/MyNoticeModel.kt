package com.polzzak_android.presentation.feature.myPage.notice.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class MyNoticeModel(
    val id: Int,
    val title: String,
    val date: LocalDateTime,
    val content: String
) : Parcelable
