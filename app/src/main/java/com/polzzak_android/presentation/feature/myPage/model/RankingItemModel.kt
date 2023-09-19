package com.polzzak_android.presentation.feature.myPage.model

import androidx.annotation.DrawableRes
import com.polzzak_android.R

enum class RankingStatus(@DrawableRes val resId: Int) {
    UP(R.drawable.ic_ranking_up),
    DOWN(R.drawable.ic_ranking_down),
    HOLD(R.drawable.ic_ranking_hold)
}

data class RankingItemModel(
    val ranking: Int = 0,
    val rankingStatus: RankingStatus = RankingStatus.HOLD,
    val nickname: String = "닉네임",
    val point: Int = 0,
    val level: Int = 0,
    val memberTypeDetail: String = "보호자",
    val profileUrl: String = "",
    val isMe: Boolean = false
) {
    val isOutOfRanking: Boolean
        get() = ranking > 30
}
