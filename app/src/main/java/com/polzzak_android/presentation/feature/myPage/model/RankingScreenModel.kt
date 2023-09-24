package com.polzzak_android.presentation.feature.myPage.model

import com.polzzak_android.data.remote.model.response.RankingDto

data class RankingScreenModel(
    val currentUserRanking: RankingItemModel,
    val rankingList: List<RankingItemModel>
)

fun RankingDto.toModel(): RankingScreenModel =
    RankingScreenModel(
        currentUserRanking = this.currentUserRanking.toModel(),
        rankingList = this.rankingList.map { it.toModel() }
    )