package com.polzzak_android.presentation.feature.myPage.model

import androidx.annotation.DrawableRes
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.response.RankingDto

enum class RankingStatus(@DrawableRes val resId: Int) {
    UP(R.drawable.ic_ranking_up),
    DOWN(R.drawable.ic_ranking_down),
    HOLD(R.drawable.ic_ranking_hold),
    NONE(0);     // 랭킹 등락을 표시하지 않을 때 사용

    companion object {
        fun getStatusOrNull(value: String): RankingStatus? {
            return RankingStatus.values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
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

fun RankingDto.CurrentUserRankingDto.toModel(): RankingItemModel =
    RankingItemModel(
        ranking = this.ranking,
        rankingStatus = RankingStatus.NONE,
        nickname = this.nickname,
        point = this.memberPoint.point,
        level = this.memberPoint.level,
        memberTypeDetail = this.memberType.detail,
        profileUrl = this.profileUrl,
        isMe = true
    )


fun RankingDto.UserRankingDto.toModel(): RankingItemModel =
    RankingItemModel(
        ranking = this.ranking,
        rankingStatus = RankingStatus.getStatusOrNull(this.rankingStatus) ?: RankingStatus.NONE,
        nickname = this.nickname,
        point = this.point,
        level = this.level,
        memberTypeDetail = this.memberTypeDetail ?: "아이",
        profileUrl = this.profileUrl,
        isMe = this.isMe
    )