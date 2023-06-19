package com.polzzak_android.presentation.main.detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.polzzak_android.presentation.component.PolzzakButton
import com.polzzak_android.presentation.compose.Gray500
import com.polzzak_android.presentation.compose.PolzzakAppTheme
import com.polzzak_android.presentation.compose.PolzzakTheme
import com.polzzak_android.presentation.main.model.MissionModel
import com.polzzak_android.presentation.main.model.StampBoard

@Composable
fun StampBoardDetailScreen() {
    var missionExpanded by remember {
        mutableStateOf(false)
    }

    val missions = listOf(
        MissionModel(1, "미션 1"),
        MissionModel(2, "미션 2"),
        MissionModel(3, "미션 3"),
        MissionModel(4, "미션 4"),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        // TODO: MainComponents로 빼기
        item {
            StampBoardHeader(title = "도장판 상세", chipText = "D+9")
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            // TODO: 조건에 따라 요청 알림 바 표시
        }

        item {
            StampBoxGridList(totalCount = 40) { index ->
                EmptyStamp(enabled = (index == 0), numberText = (index + 1).toString())
            }

            Spacer(modifier = Modifier.height(20.dp))
        }


        expandMissionList(
            missions = missions,
            expanded = missionExpanded,
            onExpandClick = { missionExpanded = !missionExpanded }
        )

        item {
            Spacer(modifier = Modifier.height(8.dp))

            RewardInfoSheet(
                rewardTitle = {
                    Text(text = "보상 제목", style = PolzzakTheme.typography.subTitle1)
                },
                rewardButton = {
                    PolzzakButton(
                        text = "쿠폰 발급하기",
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                rewardStateText = {
                    Text(
                        text = "쿠폰 안내 텍스트",
                        style = PolzzakTheme.typography.body4,
                        color = Gray500
                    )
                },
                deleteTextButton = {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "도장판 삭제하기",
                            style = PolzzakTheme.typography.body4.copy(fontWeight = FontWeight.SemiBold),
                            color = Gray500,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun StampBoardDetailScreenPreview() {
    PolzzakAppTheme {
        StampBoardDetailScreen()
    }
}
