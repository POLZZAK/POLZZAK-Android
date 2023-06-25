package com.polzzak_android.presentation.main.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.component.BlueChip
import com.polzzak_android.presentation.component.PolzzakButton
import com.polzzak_android.presentation.compose.Blue150
import com.polzzak_android.presentation.compose.Blue200
import com.polzzak_android.presentation.compose.Blue600
import com.polzzak_android.presentation.compose.Gray200
import com.polzzak_android.presentation.compose.Gray300
import com.polzzak_android.presentation.compose.Gray400
import com.polzzak_android.presentation.compose.Gray500
import com.polzzak_android.presentation.compose.PolzzakTheme
import com.polzzak_android.presentation.main.model.MissionModel
import com.polzzak_android.presentation.main.model.StampModel

@Composable
fun StampBoardHeader(
    title: String,
    chipText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "도장판 이름",
            style = PolzzakTheme.typography.title1,
            modifier = Modifier.weight(1f)
        )

        BlueChip(text = chipText)
    }
}

@Preview
@Composable
private fun StampBoardHeaderPreview() {
    StampBoardHeader(title = "도장판 상세", chipText = "D+9")
}


fun LazyListScope.expandMissionList(
    missions: List<MissionModel>,
    expanded: Boolean,
    onExpandClick: () -> Unit
) {
    item {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "미션 목록",
                    style = PolzzakTheme.typography.subTitle3
                )

                if (missions.size > 3) {
                    SeeMore(
                        toggleText = "더보기" to "더보기",
                        expandedProvider = { expanded },
                        modifier = Modifier.clickable(onClick = onExpandClick)
                    )
                }
            }
        }
    }

    items(
        items = if (expanded) missions else missions.take(3),
        key = { it.id }
    ) { mission ->
        Surface(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = mission.content,
                style = PolzzakTheme.typography.body3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 3.dp)
                    .padding(bottom = 12.dp)
            )
        }
    }

    // empty footer
    item {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        ) {
        }
    }
}

@Preview
@Composable
private fun ExpandMissionListPreview() {
    LazyColumn {
        expandMissionList(
            missions = listOf(
                MissionModel(1, "미션 1"),
                MissionModel(2, "미션 2"),
                MissionModel(3, "미션 3"),
                MissionModel(4, "미션 4"),
            ),
            expanded = false,
            onExpandClick = {}
        )
    }
}

@Composable
fun RewardInfoSheet(
    rewardTitle: @Composable () -> Unit,
    rewardButton: @Composable () -> Unit,
    rewardStateText: @Composable () -> Unit,
    deleteTextButton: @Composable (() -> Unit)? = null
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
        ){
            Text(
                text = "보상",
                style = PolzzakTheme.typography.subTitle3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 실제 이미지로 변경하기
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color = Blue200, shape = CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            rewardTitle()

            Spacer(modifier = Modifier.height(26.dp))

            rewardButton()

            Spacer(modifier = Modifier.height(14.dp))

            // TODO: 글자 넘침??
            rewardStateText()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                deleteTextButton?.invoke()
            }
        }
    }
}

@Preview
@Composable
fun RewardInfoSheetPreview() {
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

@Composable
fun StampBoxGridList(
    totalCount: Int,
    itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit
) {
    val isExpandButtonVisible = totalCount >= 40    // 펼치기 버튼 표시 여부
    val columnCount: Int = getColumnCount(stampCount = totalCount)  // 열 개수
    val itemsHorizontalGap: Dp = getHorizontalGap(columnCount = columnCount)    // 도장 칸들 사이 간격 수치

    var expanded by remember { mutableStateOf(false) }  // 펼침 여부

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(itemsHorizontalGap),
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 700.dp)
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .background(
                shape = RoundedCornerShape(CornerSize(12.dp)),
                color = Color.White
            )
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(CornerSize(12.dp))
            )
            .animateContentSize()
    ) {
        val itemCount = getVisibleStampCount(stampCount = totalCount, expanded = expanded)

        items(
            count = itemCount,
            key = { it },
            itemContent = itemContent
        )

        if (isExpandButtonVisible) {
            // TODO: 누를때 pressed 효과 없애기
            item(span = { GridItemSpan(maxLineSpan) }) {
                ExpandToggleButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StampBoxGridListPreview() {
    StampBoxGridList(totalCount = 40) { index ->
        EmptyStamp(enabled = (index == 0), numberText = (index + 1).toString())
    }
}

// 화면 크기에 따라 일정 비율로 늘어나고 줄어드는 수식 만들기 전까지 임시 조치
private fun getColumnCount(stampCount: Int): Int = when (stampCount) {
    10, 12, 16, 20 -> 4
    25, 30, 40 -> 5
    36, 48, 60 -> 6
    else -> -1
}

// 화면 크기에 따라 일정 비율로 늘어나고 줄어드는 수식 만들기 전까지 임시 조치
private fun getHorizontalGap(columnCount: Int): Dp = when (columnCount) {
    4 -> 16.dp
    5 -> 13.dp
    6 -> 11.dp
    else -> 0.dp
}

/**
 * 스탬프 개수와 접힘 여부에 따라 보여질 스탬프 개수 반환
 */
private fun getVisibleStampCount(stampCount: Int, expanded: Boolean): Int = when (stampCount) {
    40 -> if (expanded) stampCount else 20
    48, 60 -> if (expanded) stampCount else 30
    else -> stampCount
}

/**
 * 펼치기/접기 토글 버튼
 */
@Composable
fun ExpandToggleButton(
    expanded: Boolean,
    onClick: (() -> Unit)? = null
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { onClick?.invoke() }
        )
) {
    Divider(color = Gray200, thickness = 1.dp)
    Spacer(modifier = Modifier.height(16.dp))
    SeeMore(
        toggleText = "접기" to "펼치기",
        expandedProvider = { expanded }
    )
}

@Composable
fun SeeMore(
    toggleText: Pair<String, String>,
    expandedProvider: () -> Boolean,
    modifier: Modifier = Modifier
) = Row(
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
) {
    Text(
        text = if (expandedProvider()) toggleText.first else toggleText.second,
        style = PolzzakTheme.typography.body4,
        color = Gray500
    )
    Spacer(modifier = Modifier.width(4.dp))
    Icon(
        imageVector = if (expandedProvider()) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = "Expand",
        tint = Gray300
    )
}

/**
 * 도장이 찍힌(완료된) 도장 칸
 */
@Composable
fun CompletedStamp(
    stamp: StampModel,
    onClick: ((StampModel) -> Unit)? = null
) {
    // TODO: 이미지 리소스 추가하기
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .drawBehind {
                drawCircle(color = Blue150)
            }
            .clickable(enabled = (onClick != null), onClick = { onClick?.invoke(stamp) })
    )
}

/**
 * 빈 도장 칸
 */
@Composable
fun EmptyStamp(
    enabled: Boolean,
    numberText: String,
    onStampClick: (() -> Unit)? = null
) {
    if (enabled) {
        EnabledEmptyStamp(
            numberText = numberText,
            onClick = onStampClick
        )
    } else {
        DisabledEmptyStamp(
            numberText = numberText,
            onClick = onStampClick
        )
    }
}

@Preview
@Composable
private fun EmptyStampPreview() {
    Column {
        EmptyStamp(enabled = true, numberText = "1")
        EmptyStamp(enabled = false, numberText = "1")
    }
}

/**
 * 활성화 된 빈 도장 칸
 */
@Composable
fun EnabledEmptyStamp(
    numberText: String,
    onClick: (() -> Unit)? = null
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .aspectRatio(1f)
        .drawBehind {
            drawCircle(color = Blue150)
        }
        .border(color = Blue200, width = 1.dp, shape = CircleShape)
        .clickable(enabled = (onClick != null), onClick = { onClick?.invoke() })
) {
    Text(
        text = numberText,
        style = PolzzakTheme.typography.title2,
        color = Blue600
    )
}

/**
 * 비활성화 된 빈 도장 칸
 */
@Composable
fun DisabledEmptyStamp(
    numberText: String,
    onClick: (() -> Unit)? = null
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .aspectRatio(1f)
        .drawBehind {
            drawCircle(color = Gray200)
        }
        .clickable(enabled = (onClick != null), onClick = { onClick?.invoke() })
) {
    Text(
        text = numberText,
        style = PolzzakTheme.typography.title2,
        color = Gray400
    )
}

