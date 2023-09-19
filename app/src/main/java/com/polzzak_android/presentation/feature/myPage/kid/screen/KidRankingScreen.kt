@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.polzzak_android.presentation.feature.myPage.kid.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.components.MyRankingCard
import com.polzzak_android.presentation.feature.myPage.components.RankingHeader
import com.polzzak_android.presentation.feature.myPage.components.RankingListItemBase
import com.polzzak_android.presentation.feature.myPage.components.UserNickname
import com.polzzak_android.presentation.feature.myPage.model.RankingItemModel
import com.polzzak_android.presentation.feature.myPage.model.RankingScreenModel
import com.polzzak_android.presentation.feature.myPage.model.RankingStatus
import kotlinx.coroutines.flow.StateFlow

@Composable
fun KidRankingScreen(
    data: StateFlow<ModelState<RankingScreenModel>>,
    onError: () -> Unit
) {
    val state by data.collectAsState()

    Crossfade(
        targetState = state,
        animationSpec = tween(200),
        label = "CouponDetailScreen change animation"
    ) { currentState ->
        when (currentState) {
            is ModelState.Loading -> {
                // TODO: 로딩 어색한지 보기
            }
            is ModelState.Success -> {
                KidRankingScreen(
                    currentUserRanking = currentState.data.currentUserRanking,
                    rankingList = currentState.data.rankingList
                )
            }
            is ModelState.Error -> {
                onError()
            }
        }
    }
}

@Composable
private fun KidRankingScreen(
    currentUserRanking: RankingItemModel,
    rankingList: List<RankingItemModel>
) = LazyColumn(
    modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
) {
    item {
        RankingHeader(rankingType = stringResource(id = R.string.ranking_type_kid))
    }

    // TODO: top30 안에 드냐 마냐에 따라 표시 여부 달라짐
    if (currentUserRanking.isOutOfRanking) {
        stickyHeader {
            MyRankingCard {
                RankingListItemBase(
                    ranking = currentUserRanking.ranking,
                    rankingStatus = currentUserRanking.rankingStatus,
                    point = currentUserRanking.point,
                    level = currentUserRanking.level,
                    profileUrl = currentUserRanking.profileUrl,
                ) {
                    UserNickname(isMe = true) {
                        Text(
                            text = currentUserRanking.nickname,
                            style = PolzzakTheme.typography.semiBold14
                        )
                    }
                }
            }
        }
    }

    item {
        Text(
            text = stringResource(id = R.string.ranking_top_30),
            style = PolzzakTheme.typography.semiBold18,
            color = Color.Black,
            modifier = Modifier.padding(all = 16.dp)
        )
    }

    itemsIndexed(
        items = rankingList,
        key = { index, _ -> index }
    ) { _, data ->
        RankingListItemBase(
            ranking = data.ranking,
            rankingStatus = data.rankingStatus,
            point = data.point,
            level = data.level,
            profileUrl = data.profileUrl,
        ) {
            UserNickname(isMe = data.isMe) {
                Text(
                    text = data.nickname,
                    style = PolzzakTheme.typography.semiBold14
                )
            }
        }
    }
}

@Preview
@Composable
fun KidRankingScreenPreview() {
    KidRankingScreen(
        currentUserRanking = RankingItemModel(ranking = 56),
        rankingList = List(30) {
            RankingItemModel(
                ranking = it + 1,
                rankingStatus = when {
                    (it + 1) % 2 == 0 -> RankingStatus.UP
                    (it + 1) % 3 == 0 -> RankingStatus.DOWN
                    else -> RankingStatus.HOLD
                }
            )
        }
    )
}