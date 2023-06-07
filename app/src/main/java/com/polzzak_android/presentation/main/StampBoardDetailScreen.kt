package com.polzzak_android.presentation.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.compose.Blue150
import com.polzzak_android.presentation.compose.Blue200
import com.polzzak_android.presentation.compose.Blue500
import com.polzzak_android.presentation.compose.Blue600
import com.polzzak_android.presentation.compose.Gray200
import com.polzzak_android.presentation.compose.Gray300
import com.polzzak_android.presentation.compose.Gray400
import com.polzzak_android.presentation.compose.Gray500
import com.polzzak_android.presentation.compose.PolzzakAppTheme
import com.polzzak_android.presentation.compose.PolzzakTheme

@Composable
fun StampBoardDetailScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
        color = Color.Transparent
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            /* 상단 도장판 이름과 디데이 영역 */
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "도장판 이름",
                    style = PolzzakTheme.typography.title1,
                    modifier = Modifier.weight(1f)
                )

                // TODO: 컴포넌트로 추출하기
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(corner = CornerSize(6.dp)))
                        .background(color = Blue500)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "D+9",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* 도장 동그라미 리스트 */
            StampBoxGridList(
                completedStampList = emptyList(),
                goalStampCount = 40
            )
        }
    }
}

@Composable
@Preview
private fun StampBoardDetailScreenPreview() {
    PolzzakAppTheme {
        StampBoardDetailScreen()
    }
}

@Composable
private fun StampBoxGridList(
    completedStampList: List<Int>,  // TODO: 실제 데이터 타입으로 변경하기
    goalStampCount: Int,
    onCompletedStampClick: ((stampId: String) -> Unit)? = null,
    onEmptyStampClick: (() -> Unit)? = null
) {
    val isExpandButtonVisible = goalStampCount >= 40    // 펼치기 버튼 표시 여부
    val columnCount: Int = getColumnCount(stampCount = goalStampCount)  // 열 개수
    val itemsHorizontalGap: Dp = getHorizontalGap(columnCount = columnCount)    // 도장 칸들 사이 간격 수치

    var expanded by remember { mutableStateOf(false) }  // 펼침 여부

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = columnCount),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(CornerSize(12.dp)),
                color = Color.White
            )
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(CornerSize(12.dp))
            )
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(itemsHorizontalGap)
    ) {
        val itemCount = getVisibleStampCount(stampCount = goalStampCount, expanded = expanded)

        items(
            count = itemCount,
            key = { it }
        ) { index ->
            // 완료된 도장 리스트에서 인덱스로 차례대로 꺼냈을 때
            // 데이터가 있으면 완료된 도장 칸
            // 데이터가 없으면 빈 도장 칸

            // TODO: 밖에서 크기 조정하는 Box로 감싸기
            completedStampList.getOrNull(index)?.let {
                CompletedStamp(
                    onClick = { onCompletedStampClick?.invoke(it.toString()) }
                )
            } ?: kotlin.run {
                EmptyStamp(
                    enabled = (index == completedStampList.size),   // 마지막으로 찍힌 도장 다음 칸이 활성화되어야 함
                    numberText = (index + 1).toString(),
                    onEnabledStampClick = onEmptyStampClick
                )
            }
        }

        if (isExpandButtonVisible) {
            // TODO: 누를때 pressed 효과 없애기
            item(span = { GridItemSpan(columnCount) }) {
                ExpandToggleButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
        }
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
private fun ExpandToggleButton(
    expanded: Boolean,
    onClick: (() -> Unit)? = null
) = Column(
    modifier = Modifier.clickable { onClick?.invoke() }
) {
    Divider()

    val text = if (expanded) "접기" else "펼치기"      // TODO: StringResource로 정의하기
    val icon = if (expanded) {                      // TODO: 리소스 받아서 넣기
        Icons.Default.KeyboardArrowUp
    } else {
        Icons.Default.KeyboardArrowDown
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(text = text, style = PolzzakTheme.typography.body4, color = Gray500)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(imageVector = icon, contentDescription = "Expand", tint = Gray300)
    }
}

/**
 * 도장이 찍힌(완료된) 도장 칸
 */
@Composable
private fun CompletedStamp(
    // TODO: 도장 이미지 파라미터 추가하기
    onClick: (() -> Unit)? = null
) {
    // 해당 컴포저블에서 도장 id 외에 다른 정보도 가지고 있어야 하는지?
    // 도장 상세 정보 표시할 때 그냥 api 찔러서 정보 받아오는지?
}

/**
 * 빈 도장 칸
 */
@Composable
private fun EmptyStamp(
    enabled: Boolean,
    numberText: String,
    onEnabledStampClick: (() -> Unit)? = null
) {
    if (enabled) {
        EnabledEmptyStamp(
            numberText = numberText,
            onClick = onEnabledStampClick
        )
    } else {
        DisabledEmptyStamp(numberText = numberText)
    }
}

/**
 * 활성화 된 빈 도장 칸
 */
@Composable
private fun EnabledEmptyStamp(
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
        .clickable { onClick?.invoke() }
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
private fun DisabledEmptyStamp(
    numberText: String
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .aspectRatio(1f)
        .drawBehind {
            drawCircle(color = Gray200)
        }
) {
    Text(
        text = numberText,
        style = PolzzakTheme.typography.title2,
        color = Gray400
    )
}

@Composable
@Preview
private fun StampBoxGridListPreview() {
    StampBoxGridList(
        completedStampList = emptyList(),
        goalStampCount = 40
    )
}

@Composable
@Preview
private fun EmptyStampPreview() {
    Column {
        EmptyStamp(enabled = true, numberText = "1")
        EmptyStamp(enabled = false, numberText = "1")
    }
}