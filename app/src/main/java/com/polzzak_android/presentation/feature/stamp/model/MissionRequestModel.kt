package com.polzzak_android.presentation.feature.stamp.model

import com.polzzak_android.data.remote.model.response.MissionRequestDto
import java.time.LocalDateTime

data class MissionRequestModel(
    val id: Int,
    val missionContent: String,
    val createdDate: LocalDateTime
)

fun MissionRequestDto.toModel(): MissionRequestModel = MissionRequestModel(
    id = this.id,
    missionContent = this.missionContent,
    createdDate = LocalDateTime.parse(this.createdDate)
)