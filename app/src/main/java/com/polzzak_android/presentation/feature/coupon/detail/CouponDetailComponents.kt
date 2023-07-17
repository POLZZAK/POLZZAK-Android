package com.polzzak_android.presentation.feature.coupon.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.Blue200
import com.polzzak_android.presentation.common.compose.Blue600
import com.polzzak_android.presentation.common.compose.Gray200
import com.polzzak_android.presentation.common.compose.Gray400
import com.polzzak_android.presentation.common.compose.Gray500
import com.polzzak_android.presentation.common.compose.Gray700
import com.polzzak_android.presentation.common.compose.Gray800
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DeliveryDayText(date: LocalDate) = Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Row {
        Text(
            text = date.format(DateTimeFormatter.ofPattern("yyyy. MM. dd")),
            style = PolzzakTheme.typography.semiBold14,
            color = Color.White
        )
        Text(
            text = " 까지",
            style = PolzzakTheme.typography.semiBold14,
            color = Gray700
        )
    }

    Text(
        text = "선물을 전달하기로 약속했어요!",
        style = PolzzakTheme.typography.semiBold14,
        color = Gray700
    )
}

@Preview
@Composable
private fun DeliveryDayTextPreview() {
    DeliveryDayText(date = LocalDate.now())
}

@Composable
fun DeliveryCompletedText() = Text(
    text = "선물 전달 완료",
    style = PolzzakTheme.typography.semiBold14,
    color = Color.White,
    modifier = Modifier
        .background(color = Blue600, shape = RoundedCornerShape(100.dp))
        .padding(horizontal = 12.dp, vertical = 6.dp)
)

@Preview
@Composable
private fun DeliveryCompletedTextPreview() {
    DeliveryCompletedText()
}

/**
 * 쿠폰 티켓 모양의 컴포저블.
 *
 * @param header [CouponHeaderContent]를 사용하여 구현
 * @param body [CouponBodyContent]를 사용하여 구현
 * @param footer [CouponFooterContent]를 사용하여 구현
 */
@Composable
fun CouponTicket(
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    CouponBackground(
        modifier = Modifier
            .drawBehind {
                drawCouponOutLine(Arrangement.Top)
                drawCouponDashedLine(Arrangement.Bottom)
            }
            .padding(top = 26.dp, bottom = 24.dp),
        content = header
    )

    CouponBackground(
        modifier = Modifier
            .drawBehind {
                drawCouponDashedLine(Arrangement.Top)
                drawCouponDashedLine(Arrangement.Bottom)
            }
            .padding(top = 24.dp, bottom = 24.dp),
        content = body
    )

    CouponBackground(
        modifier = Modifier
            .drawBehind {
                drawCouponDashedLine(Arrangement.Top)
                drawCouponOutLine(Arrangement.Bottom)
            }
            .padding(top = 24.dp, bottom = 26.dp),
        content = footer
    )
}

/**
 * 위 혹은 아래에 쿠폰 아웃라인을 그리는 메서드
 */
private fun DrawScope.drawCouponOutLine(
    verticalArrangement: Arrangement.Vertical
) {
    val yPosition = when (verticalArrangement) {
        Arrangement.Top -> 3.dp.toPx()
        Arrangement.Bottom -> size.height - 3.dp.toPx()
        else -> 3.dp.toPx()
    }

    drawLine(
        color = Blue200,
        start = Offset(0f, yPosition),
        end = Offset(size.width, yPosition),
        strokeWidth = 6.dp.toPx()
    )
}

/**
 * 위 혹은 아래에 쿠폰 절취선을 그리는 메서드
 */
private fun DrawScope.drawCouponDashedLine(
    verticalArrangement: Arrangement.Vertical
) {
    val yPosition = when (verticalArrangement) {
        Arrangement.Top -> 0f
        Arrangement.Bottom -> size.height
        else -> 0f
    }

    drawLine(
        color = Gray200,
        start = Offset(16.dp.toPx(), yPosition),
        end = Offset(size.width - 12.dp.toPx(), yPosition),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 30f)),
        cap = StrokeCap.Round
    )
}

/**
 * 쿠폰 헤더에 들어갈 컴포저블
 */
@Composable
fun CouponHeaderContent(
    title: String
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 26.dp)
) {
    Text(
        text = "Reward",
        color = Blue600,
        style = PolzzakTheme.typography.semiBold16
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = title,
        color = Gray800,
        style = PolzzakTheme.typography.semiBold20,
        maxLines = 2,
        overflow = TextOverflow.Clip,
        textAlign = TextAlign.Center
    )
}

/**
 * 쿠폰 바디에 들어갈 컴포저블
 */
@Composable
fun CouponBodyContent(
    giverProfileUrl: String,
    giverName: String,
    receiverProfileUrl: String,
    receiverName: String,
    completedMissionCount: Int,
    stampCount: Int,
    dateCount: Int,
    onMissionClick: () -> Unit  // TODO: 상황 보고 람다 파라미터 수정하기
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 26.dp)
) {
    UserProfile(profileUrl = giverProfileUrl, label = "주는 사람", name = giverName)
    Spacer(modifier = Modifier.height(21.dp))
    UserProfile(profileUrl = receiverProfileUrl, label = "받는 사람", name = receiverName)

    Divider(
        color = Gray200,
        thickness = 2.dp,
        modifier = Modifier.padding(top = 23.dp, bottom = 20.dp)
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        LabeledCounter(label = "완료 미션", value = completedMissionCount, unit = "개", onClick = onMissionClick)
        LabeledCounter(label = "모은 도장", value = stampCount, unit = "개")
        LabeledCounter(label = "걸린 기간", value = dateCount, unit = "일")
    }
}

/**
 * 쿠폰 바디에서 사용하는 사용자 프로필 표시 컴포저블
 */
@Composable
private fun UserProfile(
    profileUrl: String,
    label: String,
    name: String
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth()
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(color = Blue200, shape = CircleShape)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Column {
        Text(text = label, color = Gray500, style = PolzzakTheme.typography.medium13)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = name,
            color = Gray800,
            style = PolzzakTheme.typography.semiBold16,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 쿠폰 바디에서 사용하는 도장판 관련 카운터 컴포저블
 */
@Composable
private fun LabeledCounter(
    label: String,
    value: Int,
    unit: String,
    onClick: (() -> Unit)? = null
) = Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                enabled = (onClick != null),
                onClick = { onClick?.invoke() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Text(text = label, color = Gray500, style = PolzzakTheme.typography.medium13)
        if (onClick != null) {
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "click",
                tint = Gray400,
                modifier = Modifier.size(12.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
    Row {
        Text(
            text = value.toString(),
            color = Blue600,
            style = PolzzakTheme.typography.semiBold18,
            modifier = Modifier.alignByBaseline()
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = unit,
            color = Blue600,
            style = PolzzakTheme.typography.semiBold12,
            modifier = Modifier.alignByBaseline()
        )
    }
}

/**
 * 쿠폰 푸터에 들어갈 컴포저블
 */
@Composable
fun CouponFooterContent(
    startDate: LocalDate,
    endDate: LocalDate
) = Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 36.dp)
) {
    MissionDate(label = "미션 시작일", date = startDate)
    MissionDate(label = "미션 완료일", date = endDate)
}

/**
 * 쿠폰 푸터에서 사용하는 미션 날짜 표시 컴포저블
 */
@Composable
private fun MissionDate(
    label: String,
    date: LocalDate
) = Column {
    Text(text = label, color = Gray500, style = PolzzakTheme.typography.medium13)
    Spacer(modifier = Modifier.height(2.dp))

    val dateText = date.format(DateTimeFormatter.ofPattern("yyyy. MM. dd"))
    Text(text = dateText, color = Gray800, style = PolzzakTheme.typography.semiBold18)
}

@Preview(device = "spec:width=500dp,height=891dp", showBackground = false)
@Composable
private fun CouponTicketPreview() {
    Scaffold(
        backgroundColor = Color.LightGray,
    ) {
        it.toString()

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
        ) {
            CouponTicket(
                header = {
                    CouponHeaderContent(title = "아이유 2023 콘서트 티켓 아이유 2023 콘서트 티켓 구해달라")
                },
                body = {
                       CouponBodyContent(
                           giverProfileUrl = "",
                           giverName = "홍길동",
                           receiverProfileUrl = "",
                           receiverName = "김수한무거북이와두루미삼천갑자동방삭",
                           completedMissionCount = 8,
                           stampCount = 16,
                           dateCount = 40,
                           onMissionClick = {}
                       )
                },
                footer = {
                    CouponFooterContent(
                        startDate = LocalDate.now().minusDays(40),
                        endDate = LocalDate.now()
                    )
                }
            )
        }
    }
}

/**
 * 쿠폰 티켓 각 영역의 background 컴포저블.
 *
 * 화면 크기가 작으면 너비가 그만큼 작아지지만, 커지면 최대 323dp 만큼만 늘어남.
 */
@Composable
private fun CouponBackground(
    modifier: Modifier,
    content: @Composable () -> Unit
) = Box(
    modifier = Modifier
        .sizeIn(maxWidth = 323.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(shape = RoundedCornerShape(12.dp))
        .background(color = Color.White)
        .then(modifier),
    contentAlignment = Alignment.TopCenter
) {
    content()
}