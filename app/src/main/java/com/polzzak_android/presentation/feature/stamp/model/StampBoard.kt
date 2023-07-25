package com.polzzak_android.presentation.feature.stamp.model

data class StampBoard(
    val type: Int,      // 1 non 2 yes
    val partner: Partner,
    val stampBoardSummaries: List<StampBoardSummary>
)