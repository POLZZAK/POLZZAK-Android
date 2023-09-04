package com.polzzak_android.presentation.feature.stamp.model

data class StampBoardModel(
    val type: Int,      // 1 non 2 yes
    val partner: PartnerModel?,
    val stampBoardSummaries: List<StampBoardSummaryModel>?
)

data class PartnerModel(
    val isKid: Boolean = false,
    val memberId: Int = -1,
    val memberType: String = "",
    val nickname: String = "",
    val profileUrl: String = ""
)

data class StampBoardSummaryModel(
    val currentStampCount: Int,
    val goalStampCount: Int,
    val missionRequestCount: Int,
    val name: String,
    val reward: String,
    val stampBoardId: Int
)