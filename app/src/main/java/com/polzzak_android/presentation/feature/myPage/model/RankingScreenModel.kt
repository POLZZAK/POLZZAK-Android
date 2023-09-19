package com.polzzak_android.presentation.feature.myPage.model

data class RankingScreenModel(
    val currentUserRanking: RankingItemModel,
    val rankingList: List<RankingItemModel>
)