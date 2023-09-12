package com.polzzak_android.presentation.feature.coupon.detail

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.common.compose.Blue600
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getRemainingSeconds
import com.polzzak_android.presentation.component.PolzzakButton
import com.polzzak_android.presentation.component.PolzzakWhiteButton
import com.polzzak_android.presentation.component.polzzakButtonColors
import com.polzzak_android.presentation.coupon.model.CouponState
import com.polzzak_android.presentation.feature.coupon.model.CouponDetailModel
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun CouponDetailScreen_Protector(
    couponDetailData: StateFlow<ModelState<CouponDetailModel>>,
    onMissionClick: (missions: List<String>) -> Unit,
) {
    val state by couponDetailData.collectAsState()

    Crossfade(
        targetState = state,
        animationSpec = tween(200),
        label = "CouponDetailScreen change animation"
    ) { currentState ->
        when (currentState) {
            is ModelState.Success -> {
                CouponDetailScreen(data = currentState.data, onMissionClick = onMissionClick)
            }
            else -> {
                // Fragment에서 로딩, 에러 상태 화면 처리 중
            }
        }
    }

}

@Composable
fun CouponDetailScreen_Kid(
    couponDetailData: StateFlow<ModelState<CouponDetailModel>>,
    onMissionClick: (missions: List<String>) -> Unit,
    onRewardRequestClick: (couponId: Int) -> Unit,
    onRewardDeliveredClick: (couponId: Int) -> Unit
) {
    val state by couponDetailData.collectAsState()

    Crossfade(
        targetState = state,
        animationSpec = tween(200),
        label = "CouponDetailScreen change animation"
    ) { currentState ->
        when (currentState) {
            is ModelState.Success -> {
                CouponDetailScreen(
                    data = currentState.data,
                    onMissionClick = onMissionClick,
                    buttons = {
                        // 조르기 시간이 없으면 조르기 버튼 표시
                        // 조르기 시간이 있으면 카운트다운 표시
                        Box(modifier = Modifier.weight(1f)) {
                            var countdownVisibility by remember {
                                mutableStateOf(currentState.data.rewardRequestDate != null)
                            }

                            PolzzakButton(
                                onClick = { onRewardRequestClick(currentState.data.couponId) },
                                colors = ButtonDefaults.polzzakButtonColors(backgroundColor = Blue600),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "선물 조르기")
                            }

                            if (countdownVisibility) {
                                CountdownButton(
                                    remainingSeconds = currentState.data.rewardRequestDate!!.getRemainingSeconds(),
                                    modifier = Modifier.fillMaxWidth(),
                                    onCountEnd = { countdownVisibility = false }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(7.dp))
                        PolzzakWhiteButton(
                            onClick = { onRewardDeliveredClick(currentState.data.couponId) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "선물 받기 완료")
                        }
                    }
                )
            }
            else -> {
                // Fragment에서 로딩, 에러 상태 화면 처리 중
            }
        }
    }
}

@Composable
private fun CouponDetailScreen(
    data: CouponDetailModel,
    onMissionClick: (missions: List<String>) -> Unit,
    buttons: @Composable (RowScope.() -> Unit)? = null
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxSize()
        .padding(top = 26.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
) {
    CouponTicket(
        header = {
            CouponHeaderContent(title = data.rewardTitle)
        },
        body = {
            CouponBodyContent(
                giverName = data.giverName,
                giverProfileUrl = data.giverProfileUrl,
                receiverName = data.receiverName,
                receiverProfileUrl = data.receiverProfileUrl,
                completedMissionCount = data.missions.size,
                stampCount = data.stampCount,
                dateCount = Duration.between(
                    data.startDate.atStartOfDay(),
                    data.endDate.atStartOfDay()
                ).toDays().toInt(),
                onMissionClick = { onMissionClick(data.missions) }
            )
        },
        footer = {
            CouponFooterContent(startDate = data.startDate, endDate = data.endDate)
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    )

    when (data.state) {
        CouponState.ISSUED -> {     // 쿠폰 발급 완료 상태
            Spacer(modifier = Modifier.height(30.dp))
            DeliveryDayText(date = data.rewardDate ?: LocalDate.now())

            if (buttons != null) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    content = buttons
                )
            }
        }
        CouponState.REWARDED -> {   // 상품 수령 완료 상태
            Spacer(modifier = Modifier.height(42.dp))
            DeliveryCompletedText()
        }
    }
}

@Preview
@Composable
fun CouponDetailScreenPreview() {
    CouponDetailScreen(
        data = CouponDetailModel(
            couponId = 0,
            state = CouponState.ISSUED,
            rewardTitle = "쿠폰 샘플",
            giverName = "홍길동",
            giverProfileUrl = "",
            receiverName = "김철수",
            receiverProfileUrl = "",
            missions = listOf("1", "2", "3"),
            stampCount = 16,
            startDate = LocalDate.now().minusDays(7),
            endDate = LocalDate.now(),
            rewardRequestDate = LocalDateTime.now().minusMinutes(30),
            rewardDate = LocalDate.now()
        ),
        onMissionClick = {},
        buttons = {
            Box(modifier = Modifier.weight(1f)) {
                var countdownVisibility by remember {
                    mutableStateOf(true)
                }

                PolzzakButton(
                    onClick = {},
                    colors = ButtonDefaults.polzzakButtonColors(backgroundColor = Blue600),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "선물 조르기")
                }

                if (countdownVisibility) {
                    CountdownButton(
                        remainingSeconds = 5,
                        modifier = Modifier.fillMaxWidth(),
                        onCountEnd = { countdownVisibility = false }
                    )
                }
            }
            Spacer(modifier = Modifier.width(7.dp))
            PolzzakWhiteButton(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "선물 받기 완료")
            }
        }
    )
}