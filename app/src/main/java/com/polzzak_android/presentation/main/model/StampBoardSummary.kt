package com.polzzak_android.presentation.main.model

data class StampBoardSummary(
    val currentStampCount: Int,
    val goalStampCount: Int,
    val isRewarded: Boolean,
    val missionCompleteCount: Int,
    val name: String,
    val reward: String,
    val stampBoardId: Int
)