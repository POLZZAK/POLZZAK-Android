package com.polzzak_android.presentation.main.model

import com.polzzak_android.data.remote.model.response.StampBoardDetailDto
import com.polzzak_android.presentation.common.util.toLocalDate
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class StampBoardDetailModel(
    val stampBoardStatus: StampBoardStatus,
    val boardTitle: String,
    val dateCount: Int,
    val totalStampCount: Int,
    val stampList: List<StampModel>,
    val missionList: List<MissionModel>,
    val missionRequestList: List<MissionRequestModel>,
    val rewardTitle: String,
)

fun StampBoardDetailDto.toModel(): StampBoardDetailModel = StampBoardDetailModel(
    stampBoardStatus = StampBoardStatus.getStatus(this.status) ?: StampBoardStatus.PROGRESS,
    boardTitle = this.name,
    dateCount = Duration.between(
        this.createdDate.toLocalDate(),
        this.completedDate.toLocalDate() ?: LocalDate.now()
    ).toDays().toInt(),
    totalStampCount = this.goalStampCount,
    stampList = this.stamps.map { it.toModel() },
    missionList = this.missions.map { it.toModel() },
    missionRequestList = this.missionRequestList.map { it.toModel() },
    rewardTitle = this.reward
)