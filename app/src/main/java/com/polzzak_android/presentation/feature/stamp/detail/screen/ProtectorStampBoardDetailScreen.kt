package com.polzzak_android.presentation.feature.stamp.detail.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.component.NoticeBar
import com.polzzak_android.presentation.component.PolzzakButton
import com.polzzak_android.presentation.common.compose.Gray500
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardStatus
import com.polzzak_android.presentation.feature.stamp.model.StampModel

/**
 * 부모 도장판 상세 화면
 *
 * @param stampBoardStatus 도장판 상태 enum
 * @param boardTitle 도장판 이름
 * @param dateCount 도장판 생성일로부터 오늘까지 혹은 도장판 완성일까지의 날짜 카운트
 * @param isStampRequested 도장 요청 여부
 * @param totalStampCount 목표 도장 개수
 * @param stampList 찍힌 도장 리스트
 * @param onStampClick 찍힌 도장 눌렀을 때
 * @param onEmptyStampClick 빈 도장 칸 눌렀을 때
 * @param missionList 해당 도장판의 미션 리스트
 * @param rewardTitle 보상 제목
 * @param onRewardButtonClick 쿠폰 발급 버튼 눌렀을 때
 * @param onBoardDeleteClick 도장판 삭제 눌렀을 때
 */
@Composable
fun StampBoardDetailScreen_Protector(
    stampBoardStatus: StampBoardStatus,     // 도장판 상태
    boardTitle: String,                     // 도장판 이름
    dateCount: Int,                         // 날짜 카운트
    isStampRequested: Boolean,            // 도장 요청 있는지
    totalStampCount: Int,                   // 도장 전체 개수
    stampList: List<StampModel>,            // 도장 리스트
    onStampClick: (StampModel) -> Unit,     // 찍힌 도장 눌렀을 때
    onEmptyStampClick: () -> Unit,          // 빈 도장 칸 눌렀을 때
    missionList: List<MissionModel>,        // 미션 리스트
    rewardTitle: String,                    // 보상 제목
    onRewardButtonClick: () -> Unit,        // 보상 버튼 눌렀을 때
    onBoardDeleteClick: () -> Unit          // 도장판 삭제 눌렀을 때
) {
    var missionListExpanded by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            val chipText = when (stampBoardStatus) {
                StampBoardStatus.PROGRESS -> "D+${dateCount}"
                else -> "${dateCount}일 걸렸어요!"
            }

            StampBoardHeader(title = boardTitle, chipText = chipText)

            if (isStampRequested) {
                Box(modifier = Modifier.padding(16.dp)) {
                    NoticeBar(text = "도장 요청이 있어요!")
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
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
                        enabled = getCouponButtonEnable(stampBoardStatus),
                        modifier = Modifier.fillMaxWidth()
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
                },
                deleteTextButton = {
                    TextButton(onClick = onBoardDeleteClick) {
                        Text(
                            text = "도장판 삭제하기",
                            style = PolzzakTheme.typography.medium13.copy(fontWeight = FontWeight.SemiBold),
                            color = Gray500,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            )
        }
    }
}

private fun getCouponButtonText(stampBoardStatus: StampBoardStatus): String {
    return when (stampBoardStatus) {
        StampBoardStatus.PROGRESS, StampBoardStatus.COMPLETED -> "쿠폰 발급하기"
        else -> "쿠폰 발급 완료"
    }
}

private fun getCouponButtonEnable(stampBoardStatus: StampBoardStatus): Boolean {
    return stampBoardStatus == StampBoardStatus.COMPLETED
}

private fun getCouponInfoText(stampBoardStatus: StampBoardStatus): String {
    return when (stampBoardStatus) {
        StampBoardStatus.PROGRESS -> "도장판이 다 채워지면 쿠폰을 발급해줄 수 있어요."
        StampBoardStatus.COMPLETED -> "도장판이 다 채워졌어요! 쿠폰을 발급해주세요."
        else -> "내 쿠폰함에서 확인하세요."
    }
}

@Preview
@Composable
fun StampBoardDetailScreen_ProtectorPreview() {
    StampBoardDetailScreen_Protector(
        stampBoardStatus = StampBoardStatus.PROGRESS,
        boardTitle = "도장판 이름",
        dateCount = 4,
        isStampRequested = true,
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
        onRewardButtonClick = { /*TODO*/ },
        onBoardDeleteClick = { /*TODO*/ }
    )
}