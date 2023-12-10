package com.polzzak_android.presentation.feature.stamp.model

import com.polzzak_android.data.remote.model.response.MissionRequestDto
import com.polzzak_android.presentation.common.util.toLocalDateTimeOrNull
import java.time.LocalDateTime

data class MissionRequestModel(
    val id: Int,
    val missionContent: String,
    val createdDate: LocalDateTime
) : MissionData

fun MissionRequestDto.toModel(): MissionRequestModel = MissionRequestModel(
    id = this.id,
    missionContent = this.missionContent,
    createdDate = this.createdDate.toLocalDateTimeOrNull() ?: LocalDateTime.now()
)