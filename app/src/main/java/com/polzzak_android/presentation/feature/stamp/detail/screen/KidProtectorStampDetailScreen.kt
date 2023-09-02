package com.polzzak_android.presentation.feature.stamp.detail.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.component.PolzzakButton
import com.polzzak_android.presentation.common.compose.Gray500
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardDetailModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardStatus
import com.polzzak_android.presentation.feature.stamp.model.StampModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun StampBoardDetailScreen_Kid(
    stampBoardData: StateFlow<ModelState<StampBoardDetailModel>>,
    onStampClick: (StampModel) -> Unit,
    onEmptyStampClick: () -> Unit,
    onRewardButtonClick: () -> Unit
) {
    val state by stampBoardData.collectAsState()

    Crossfade(
        targetState = state,
        animationSpec = tween(400),
        label = "StampBoardDetailScreen change animation"
    ) { currentState ->
        when (currentState) {
            is ModelState.Loading -> {
                // 스켈레톤
                StampBoardDetailSkeleton()
            }
            is ModelState.Success -> {
                StampBoardDetailScreen_Kid(
                    stampBoardStatus = currentState.data.stampBoardStatus,
                    boardTitle = currentState.data.boardTitle,
                    dateCount = currentState.data.dateCount,
                    totalStampCount = currentState.data.totalStampCount,
                    stampList = currentState.data.stampList,
                    onStampClick = onStampClick,
                    onEmptyStampClick = onEmptyStampClick,
                    missionList = currentState.data.missionList,
                    rewardTitle = currentState.data.rewardTitle,
                    onRewardButtonClick = onRewardButtonClick
                )
            }
            is ModelState.Error -> {
                // ?
            }
        }
    }

}

/**
 * 아이 도장판 상세 화면
 *
 * @param stampBoardStatus 도장판 상태 enum
 * @param boardTitle 도장판 이름
 * @param dateCount 도장판 생성일로부터 오늘까지 혹은 도장판 완성일까지의 날짜 카운트
 * @param totalStampCount 목표 도장 개수
 * @param stampList 찍힌 도장 리스트
 * @param onStampClick 찍힌 도장 눌렀을 때
 * @param onEmptyStampClick 빈 도장 칸 눌렀을 때
 * @param missionList 해당 도장판의 미션 리스트
 * @param rewardTitle 보상 제목
 * @param onRewardButtonClick 쿠폰 발급 버튼 눌렀을 때
 */
@Composable
private fun StampBoardDetailScreen_Kid(
    stampBoardStatus: StampBoardStatus,
    boardTitle: String,
    dateCount: Int,
    totalStampCount: Int,
    stampList: List<StampModel>,
    onStampClick: (StampModel) -> Unit,
    onEmptyStampClick: () -> Unit,
    missionList: List<MissionModel>,
    rewardTitle: String,
    onRewardButtonClick: () -> Unit
) {
    var missionListExpanded by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            val chipText = when (stampBoardStatus) {
                StampBoardStatus.PROGRESS -> "D+${dateCount}"
                else -> "${dateCount}일 걸렸어요!"
            }

            StampBoardHeader(title = boardTitle, chipText = chipText)

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            StampBoxGridList(totalCount = totalStampCount) { index ->
                // 총 도장 개수만큼 도장칸 생성
                // 도장 리스트에서 index로 꺼냈을 때 없으면 빈 칸
                stampList.getOrNull(index)?.also { stamp ->
                    CompletedStamp(stamp = stamp, onClick = onStampClick)
                } ?: kotlin.run {
                    DisabledEmptyStamp(
                        numberText = index.plus(1).toString(),
                        onClick = onEmptyStampClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        expandMissionList(
            missions = missionList,
            expanded = missionListExpanded,
            onExpandClick = { missionListExpanded = !missionListExpanded }
        )

        item {
            Spacer(modifier = Modifier.height(8.dp))

            RewardInfoSheet(
                rewardTitle = {
                    Text(text = rewardTitle, style = PolzzakTheme.typography.semiBold18)
                },
                rewardButton = {
                    PolzzakButton(
                        onClick = onRewardButtonClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = getCouponButtonEnable(stampBoardStatus)
                    ) {
                        Text(text = getCouponButtonText(stampBoardStatus))
                    }
                },
                rewardStateText = {
                    Text(
                        text = getCouponInfoText(stampBoardStatus),
                        style = PolzzakTheme.typography.medium13,
                        color = Gray500
                    )
                }
            )
        }
    }
}

private fun getCouponButtonText(stampBoardStatus: StampBoardStatus): String {
    return when (stampBoardStatus) {
        StampBoardStatus.REWARDED -> "쿠폰 발급 완료"
        else -> "쿠폰 받기"
    }
}

private fun getCouponButtonEnable(stampBoardStatus: StampBoardStatus): Boolean {
    return stampBoardStatus == StampBoardStatus.ISSUED_COUPON
}

private fun getCouponInfoText(stampBoardStatus: StampBoardStatus): String {
    return when (stampBoardStatus) {
        StampBoardStatus.PROGRESS -> "도장판을 다 채우면 쿠폰을 받을 수 있어요."
        StampBoardStatus.COMPLETED -> "보호자가 쿠폰을 발급해줄 때까지 잠시만 기다려주세요."
        StampBoardStatus.ISSUED_COUPON -> "선물 쿠폰이 도착했어요!"
        StampBoardStatus.REWARDED -> "내 쿠폰함에서 확인하세요."
    }
}

@Preview
@Composable
fun StampBoardDetailScreen_KidPreview() {
    StampBoardDetailScreen_Kid(
        stampBoardStatus = StampBoardStatus.PROGRESS,
        boardTitle = "도장판 이름",
        dateCount = 4,
        totalStampCount = 40,
        stampList = listOf(StampModel(), StampModel()),
        onStampClick = {},
        onEmptyStampClick = { /*TODO*/ },
        missionList = listOf(
            MissionModel(1, "미션 1"),
            MissionModel(2, "미션 2"),
            MissionModel(3, "미션 3"),
            MissionModel(4, "미션 4"),
        ),
        rewardTitle = "보상 타이틀",
        onRewardButtonClick = {}
    )
}