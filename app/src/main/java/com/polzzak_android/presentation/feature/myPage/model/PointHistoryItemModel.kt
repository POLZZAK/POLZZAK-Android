package com.polzzak_android.presentation.feature.myPage.model

import com.polzzak_android.data.remote.model.response.PointHistoryDto
import com.polzzak_android.presentation.common.util.toLocalDateTimeOrNull
import com.polzzak_android.presentation.common.util.toNotificationDateString

data class PointHistoryItemModel(
    val title: String,
    val increasedPoint: Int,
    val remainingPoint: Int,
    val createdDate: String
)

fun PointHistoryDto.PointHistoryContentDto.toModel(): PointHistoryItemModel =
    PointHistoryItemModel(
        title = this.description,
        increasedPoint = this.increasedPoint,
        remainingPoint = this.remainingPoint,
        createdDate = this.createdDate.toLocalDateTimeOrNull()?.toNotificationDateString() ?: ""
    )
