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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.compose.Blue100
import com.polzzak_android.presentation.compose.Blue150
import com.polzzak_android.presentation.compose.Blue200
import com.polzzak_android.presentation.compose.Blue400
import com.polzzak_android.presentation.compose.Blue500
import com.polzzak_android.presentation.compose.Blue600
import com.polzzak_android.presentation.compose.Blue700
import com.polzzak_android.presentation.compose.Gray200
import com.polzzak_android.presentation.compose.Gray300
import com.polzzak_android.presentation.compose.Gray400
import com.polzzak_android.presentation.compose.Gray500
import com.polzzak_android.presentation.compose.PolzzakAppTheme
import com.polzzak_android.presentation.compose.PolzzakTheme

@Composable
fun StampBoardDetailScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
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

                BlueChip(text = "D+9")
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            // TODO: 조건에 따라 요청 알림 바 표시
        }

        item {
            StampBoxGridList(
                completedStampList = emptyList(),
                goalStampCount = 60
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))

            Surface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Text(text = "미션 목록", style = PolzzakTheme.typography.subTitle3)
                    SeeMore(toggleText = "접기" to "더보기", expandedProvider = { false })
                }
            }
        }

        // TODO: 실제 데이터 리스트로 바꾸기, 펼치기 기능 추가
        items(
            count = 3,
            key = { it },
        ) {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "미션 내용 $it",
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

        item {
            Spacer(modifier = Modifier.height(8.dp))

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

                    Text(text = "보상 제목", style = PolzzakTheme.typography.subTitle1)

                    Spacer(modifier = Modifier.height(26.dp))

                    PolzzakButton(
                        text = "쿠폰 발급하기",
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "쿠폰 안내 텍스트",
                        style = PolzzakTheme.typography.body4,
                        color = Gray500
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        // TODO: 조건 따라서 visibility 설정하기
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(
                                text = "도장판 삭제하기",
                                style = PolzzakTheme.typography.body4.copy(fontWeight = FontWeight.SemiBold),
                                color = Gray500,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
    }
}

// TODO: 컴포넌트 공용화
@Composable
fun PolzzakButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = Button(
    onClick = onClick,
    contentPadding = PaddingValues(horizontal = 14.dp),
    shape = RoundedCornerShape(8.dp),
    colors = ButtonDefaults.buttonColors(
        backgroundColor = Blue500,
        contentColor = Color.White,
        disabledBackgroundColor = Blue200,
        disabledContentColor = Color.White
    ),
    elevation = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    ),
    modifier = Modifier
        .height(50.dp)
        .then(modifier)
) {
    Text(text = text, style = PolzzakTheme.typography.subTitle3)
}

// TODO: 공용 컴포넌트로?
@Composable
fun BlueChip(text: String) = Text(
    text = text,
    color = Color.White,
    style = PolzzakTheme.typography.subTitle3,
    modifier = Modifier
        .clip(RoundedCornerShape(corner = CornerSize(6.dp)))
        .background(color = Blue500)
        .padding(horizontal = 8.dp, vertical = 4.dp)
)

@Composable
fun NoticeBar(text: String) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Blue100, shape = RoundedCornerShape(8.dp))
        .border(width = 1.dp, color = Blue700.copy(alpha = 0.16f))
        .padding(horizontal = 16.dp, vertical = 12.dp)
) {
    Icon(
        imageVector = Icons.Default.Notifications,
        contentDescription = text,
        tint = Blue400
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = text,
        style = PolzzakTheme.typography.body2,
        color = Blue600,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
@Preview
fun NoticeBarPreview() {
    NoticeBar(text = "도장 요청이 있어요!")
}

// TODO: 보호자용(도장 활성화o), 아이용(도장 활성화x) 구분해야함
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
            item(span = { GridItemSpan(maxLineSpan) }) {
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
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick?.invoke() }
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
    modifier: Modifier = Modifier,
    expandedProvider: () -> Boolean
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
private fun StampBoardDetailScreenPreview() {
    PolzzakAppTheme {
        StampBoardDetailScreen()
    }
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

@Preview
@Composable
private fun PolzzakButtonPreview() {
    PolzzakButton(text = "확인", onClick = { /*TODO*/ })
}