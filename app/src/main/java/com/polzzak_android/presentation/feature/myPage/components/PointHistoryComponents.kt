package com.polzzak_android.presentation.feature.myPage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.Blue200
import com.polzzak_android.presentation.common.compose.Gray200
import com.polzzak_android.presentation.common.compose.Gray300
import com.polzzak_android.presentation.common.compose.Gray500
import com.polzzak_android.presentation.common.compose.Gray800
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import kotlin.math.absoluteValue

@Composable
fun PointHistoryListItem(
    title: String,
    date: String,
    increasedPoint: Int,
    remainingPoint: Int
) = Card(
    elevation = 0.dp,
    border = BorderStroke(width = 1.dp, color = Gray200),
    shape = RoundedCornerShape(8.dp),
    backgroundColor = Color.White,
    modifier = Modifier.fillMaxWidth()
) {
    val isIncreased: Boolean = increasedPoint > 0
    val contentText = if (isIncreased) "%dP 적립" else "%dP 차감"

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 적립 이미지
            Image(
                painter = painterResource(
                    id = if (isIncreased) {
                        R.drawable.ic_point_history_increase
                    } else {
                        R.drawable.ic_point_history_decrease
                    }
                ),
                contentDescription = "point image",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            
            // 본문 영역
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 제목
                    Text(
                        text = title,
                        color = Gray800,
                        style = PolzzakTheme.typography.semiBold14
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Box(modifier = Modifier
                        .size(4.dp)
                        .background(color = Gray300, shape = CircleShape))
                    Spacer(modifier = Modifier.width(7.dp))
                    // 날짜
                    Text(
                        text = date,
                        color = Gray500,
                        style = PolzzakTheme.typography.medium12
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // 적립 내역
                Text(
                    text = contentText.format(increasedPoint.absoluteValue),
                    color = Gray800,
                    style = PolzzakTheme.typography.bold16
                )
            }
        }

        // 현재 포인트
        Text(
            text = "내 포인트 ${remainingPoint}P",
            color = Gray500,
            style = PolzzakTheme.typography.semiBold14,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun PointHistoryListItemPreview() {
    Column {
        PointHistoryListItem(
            title = "연동 성공",
            date = "2일전",
            increasedPoint = 100,
            remainingPoint = 123
        )
        PointHistoryListItem(
            title = "도장판 삭제",
            date = "2일전",
            increasedPoint = -100,
            remainingPoint = 123
        )
    }
}