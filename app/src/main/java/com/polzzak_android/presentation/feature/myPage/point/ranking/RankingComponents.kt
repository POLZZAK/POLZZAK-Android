package com.polzzak_android.presentation.feature.myPage.point.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.Blue100
import com.polzzak_android.presentation.common.compose.Blue200
import com.polzzak_android.presentation.common.compose.Blue400
import com.polzzak_android.presentation.common.compose.Blue500
import com.polzzak_android.presentation.common.compose.Gray200
import com.polzzak_android.presentation.common.compose.Gray300
import com.polzzak_android.presentation.common.compose.Gray700
import com.polzzak_android.presentation.common.compose.Gray800
import com.polzzak_android.presentation.common.compose.PolzzakTheme

@Composable
fun RankingHeader() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Blue500)
        .padding(vertical = 26.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .heightIn(max = 128.dp)
            .paint(painter = painterResource(id = R.drawable.img_confetti))
    ) {
        Text(
            text = "폴짝! 랭킹",
            color = Color.White,
            style = PolzzakTheme.typography.bold22
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "보호자 회원",    // TODO: 회원 종류에 따라 텍스트 바뀌어야 함
                color = Blue500,
                style = PolzzakTheme.typography.semiBold12,
                modifier = Modifier
                    .background(color = Blue100, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TOP 30",
                color = Color.White,
                style = PolzzakTheme.typography.semiBold18
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "기준 시각",
                tint = Blue200,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "n월 n hh:MM 기준",
                color = Blue200,
                style = PolzzakTheme.typography.medium12
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun RankingHeaderPreview() {
    RankingHeader()
}

@Composable
private fun RankingUserItem() = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White)    // TODO: 지우기
        .padding(all = 16.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(24.dp)
    ) {
        Text(
            text = "1",
            color = Gray800,
            style = PolzzakTheme.typography.semiBold14
        )
        // TODO: 랭킹 등락 표시
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = Blue200)
        )
    }
    Spacer(modifier = Modifier.width(8.dp))
    // TODO: 프로필 사진으로 변경하기
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color = Blue200, shape = CircleShape)
    )
    Spacer(modifier = Modifier.width(8.dp))

    Column(
        modifier = Modifier
            .weight(1f)
            .padding(end = 14.dp)
    ) {
        // TODO: 보호자/아이 and 본인 여부에 따라 구성 바뀌어야 함
        MemberTypeTag(value = "엄마")
        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "가나다라마바사",
                style = PolzzakTheme.typography.semiBold13
            )
            Spacer(modifier = Modifier.width(8.dp))
            MeMarker()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .background(
                color = Blue100,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "12,345P",
            color = Blue400,
            style = PolzzakTheme.typography.medium12.copy(fontSize = 10.sp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "20계단",
            color = Blue500,
            style = PolzzakTheme.typography.semiBold12
        )
    }
}

@Preview
@Composable
private fun RankingUserItemPreview() {
    RankingUserItem()
}

@Composable
fun MeMarker() = Text(
    text = "나",
    color = Color.White,
    style = PolzzakTheme.typography.medium12,
    modifier = Modifier
        .background(
            color = Blue500,
            shape = CircleShape
        )
        .padding(horizontal = 5.dp, vertical = 2.dp)
)

@Composable
private fun MemberTypeTag(value: String) = Text(
    text = "$value 회원",
    color = Gray700,
    style = PolzzakTheme.typography.medium12,
    modifier = Modifier
        .background(color = Gray200, shape = RoundedCornerShape(4.dp))
        .border(width = 1.dp, color = Gray300, shape = RoundedCornerShape(4.dp))
        .padding(horizontal = 6.dp, vertical = 3.dp)

)

@Preview
@Composable
private fun MemberTypeTagPreview() {
    MemberTypeTag("엄마")
}