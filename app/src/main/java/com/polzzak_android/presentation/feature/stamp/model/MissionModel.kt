package com.polzzak_android.presentation.feature.stamp.model

import com.polzzak_android.data.remote.model.response.MissionDto

/**
 * 도장판의 미션에 대한 데이터 클래스
 */
data class MissionModel(
    val id: Int,
    val content: String
) : MissionData

fun MissionDto.toModel(): MissionModel = MissionModel(
    id = this.id,
    content = this.content
)