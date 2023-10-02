package com.polzzak_android.presentation.feature.stamp.model

import com.polzzak_android.data.remote.model.response.StampBoardDetailDto
import com.polzzak_android.presentation.common.util.dateBetween
import com.polzzak_android.presentation.common.util.toLocalDateOrNull
import java.time.Duration
import java.time.LocalDate

data class StampBoardDetailModel(
    val stampBoardId: Int,
    val stampBoardStatus: StampBoardStatus,
    val boardTitle: String,
    val dateCount: Int,
    val totalStampCount: Int,
    val stampList: List<StampModel>,
    val missionList: List<MissionModel>,
    val missionRequestList: List<MissionRequestModel>,
    val rewardTitle: String,
    val rewardDate: LocalDate?
)

fun StampBoardDetailDto.toModel(): StampBoardDetailModel = StampBoardDetailModel(
    stampBoardId = this.stampBoardId,
    stampBoardStatus = StampBoardStatus.getStatus(this.status) ?: StampBoardStatus.PROGRESS,
    boardTitle = this.name,
    dateCount = dateBetween(
        date1 = this.createdDate.toLocalDateOrNull() ?: LocalDate.now(),
        date2 = this.completedDate.toLocalDateOrNull() ?: LocalDate.now()
    ),
    totalStampCount = this.goalStampCount,
    stampList = this.stamps.map { it.toModel() },
    missionList = this.missions.map { it.toModel() },
    missionRequestList = this.missionRequestList.map { it.toModel() },
    rewardTitle = this.reward,
    rewardDate = this.rewardDate.toLocalDateOrNull()
)