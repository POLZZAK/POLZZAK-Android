package com.polzzak_android.presentation.main

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.compose.Blue150
import com.polzzak_android.presentation.compose.Blue200
import com.polzzak_android.presentation.compose.Blue500
import com.polzzak_android.presentation.compose.Blue600
import com.polzzak_android.presentation.compose.Gray100
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
            StampGridList()
            

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

/**
 * 스탬프 리스트
 */
@Composable
private fun StampGridList() {
    val totalStampCount = 40    // 서버에서 내려옴
    val isExpandButtonVisible = totalStampCount >= 40
    var expanded by remember { mutableStateOf(false) }

    val columnCount = getColumnCount(stampCount = totalStampCount)
    val itemsHorizontalGap = getHorizontalGap(columnCount = columnCount)

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
        val itemCount = getVisibleStampCount(stampCount = totalStampCount, expanded = expanded)

        items(itemCount) {
            // TODO: 데이터 리스트는 따로 내려주기 때문에
            //       인덱스로 꺼내고 없으면 숫자 표시해야 함
            StampBox(
                isStamped = false,
                enabled = false,
                text = (it + 1).toString()
            )
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

@Composable
private fun ExpandToggleButton(
    expanded: Boolean,
    onClick: (() -> Unit)? = null
) = Column(
    modifier = Modifier.clickable { onClick?.invoke() }
) {
    Divider()

    val text = if (expanded) "접기" else "펼치기"
    val icon = if (expanded) {
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

@Composable
private fun StampBox(
    isStamped: Boolean,
    enabled: Boolean,
    text: String = "",
    onClick: (() -> Unit)? = null
) {
    // TODO: 컬럼 개수에 따라 도장 크기와 텍스트 크기 전부 바뀜... 바리에이션을 미리 다 만들어놔야 하는건지..

    if (isStamped) {
        // TODO: ImageView
    } else {
        val backgroundColor = if (enabled) Blue150 else Gray200
        val backgroundBorderWidth = if (enabled) 2.dp else -1.dp    // 0dp를 줘도 border가 보임

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(color = backgroundColor)
                .border(width = backgroundBorderWidth, color = Blue200, shape = CircleShape)
                .clickable(enabled = enabled) {
                    onClick?.invoke()
                }
        ) {
            val textColor = if (enabled) Blue600 else Gray400
            Text(text = text, style = PolzzakTheme.typography.title2, color = textColor)
        }
    }
}

@Composable
@Preview
private fun StampBoxPreview() {
    Column {
        StampBox(
            isStamped = false,
            enabled = true
        )

        StampBox(
            isStamped = false,
            enabled = false
        )
    }
}