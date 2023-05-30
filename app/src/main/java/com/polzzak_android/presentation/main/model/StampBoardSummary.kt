package com.polzzak_android.presentation.main.model

import com.polzzak_android.data.remote.model.response.StampBoardListResponse

data class StampBoardSummary(
    val currentStampCount: Int,
    val goalStampCount: Int,
    val isRewarded: Boolean,
    val missionCompleteCount: Int,
    val name: String,
    val reward: String,
    val stampBoardId: Int
)

fun convertToStampBoardSummary(summaryData: StampBoardListResponse.StampBoardListResponseData.StampBoardSummaryData): StampBoardSummary {
    return StampBoardSummary(
        currentStampCount = summaryData.currentStampCount,
        goalStampCount = summaryData.goalStampCount,
        isRewarded = summaryData.missionRequestCount > 0,
        missionCompleteCount = summaryData.missionRequestCount,
        name = summaryData.name,
        reward = summaryData.reward,
        stampBoardId = summaryData.stampBoardId
    )
}