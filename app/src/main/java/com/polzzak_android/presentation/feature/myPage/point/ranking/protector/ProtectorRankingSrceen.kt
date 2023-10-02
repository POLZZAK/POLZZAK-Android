@file:OptIn(ExperimentalFoundationApi::class)

package com.polzzak_android.presentation.feature.myPage.point.ranking.protector

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
import com.polzzak_android.presentation.common.compose.Blue500
import com.polzzak_android.presentation.common.compose.Gray800
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.myPage.components.EmptyRankingText
import com.polzzak_android.presentation.feature.myPage.components.MemberTypeTag
import com.polzzak_android.presentation.feature.myPage.components.MyRankingCard
import com.polzzak_android.presentation.feature.myPage.components.RankingHeader
import com.polzzak_android.presentation.feature.myPage.components.RankingListItemBase
import com.polzzak_android.presentation.feature.myPage.components.UserNickname
import com.polzzak_android.presentation.feature.myPage.model.RankingItemModel
import com.polzzak_android.presentation.feature.myPage.model.RankingScreenModel
import com.polzzak_android.presentation.feature.myPage.model.RankingStatus
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ProtectorRankingScreen(
    data: SharedFlow<ModelState<RankingScreenModel>>,
    onError: (Exception) -> Unit
) {
    val state by data.collectAsState(initial = ModelState.Loading())

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
                ProtectorRankingScreen(
                    currentUserRanking = currentState.data.currentUserRanking,
                    rankingList = currentState.data.rankingList
                )
            }
            is ModelState.Error -> {
                onError(currentState.exception)
            }
        }
    }
}

@Composable
private fun ProtectorRankingScreen(
    currentUserRanking: RankingItemModel,
    rankingList: List<RankingItemModel>
) = LazyColumn(
    modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
) {
    item {
        RankingHeader(rankingType = stringResource(id = R.string.ranking_type_protector))
    }

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
                    MemberTypeTag(value = currentUserRanking.memberTypeDetail)
                    UserNickname(isMe = true) {
                        Text(
                            text = currentUserRanking.nickname,
                            style = PolzzakTheme.typography.semiBold13
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

    if (rankingList.isEmpty()) {
        item { EmptyRankingText() }
    } else {
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
                MemberTypeTag(value = currentUserRanking.memberTypeDetail)
                UserNickname(isMe = data.isMe) {
                    Text(
                        text = data.nickname,
                        style = PolzzakTheme.typography.semiBold13,
                        color = if (data.isMe) Blue500 else Gray800
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun KidRankingScreenPreview() {
    ProtectorRankingScreen(
        currentUserRanking = RankingItemModel(ranking = 56),
        /*rankingList = emptyList()*/
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