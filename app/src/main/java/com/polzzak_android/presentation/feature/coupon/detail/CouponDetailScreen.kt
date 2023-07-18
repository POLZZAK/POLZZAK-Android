package com.polzzak_android.presentation.feature.coupon.detail

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.common.compose.Blue500
import com.polzzak_android.presentation.common.compose.Blue600
import com.polzzak_android.presentation.component.PolzzakButton
import com.polzzak_android.presentation.component.PolzzakWhiteButton
import com.polzzak_android.presentation.component.polzzakButtonColors
import com.polzzak_android.presentation.coupon.model.CouponState
import com.polzzak_android.presentation.feature.coupon.model.CouponDetailModel
import java.time.Duration
import java.time.LocalDate

@Composable
fun CouponDetailScreen_Protector(
    data: CouponDetailModel,
    onMissionClick: () -> Unit
) {
    CouponDetailScreen(data = data, onMissionClick = onMissionClick)
}

@Preview
@Composable
private fun CouponDetailScreen_ProtectorPreview() {
    CouponDetailScreen_Protector(
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
            endDate = LocalDate.now()
        ),
        onMissionClick = {},
    )
}

@Composable
fun CouponDetailScreen_Kid(
    data: CouponDetailModel,
    onMissionClick: () -> Unit,
    onRewardRemindClick: () -> Unit,
    onRewardDeliveredClick: () -> Unit
) {
    CouponDetailScreen(
        data = data,
        onMissionClick = onMissionClick,
        buttons = {
            // TODO: 선물 조르기 요청 상태에 따라 버튼 종류가 달라짐
            PolzzakButton(
                onClick = onRewardRemindClick,
                colors = ButtonDefaults.polzzakButtonColors(backgroundColor = Blue600),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "선물 조르기")
            }
            Spacer(modifier = Modifier.width(7.dp))
            PolzzakWhiteButton(
                onClick = onRewardDeliveredClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "선물 받기 완료")
            }
        }
    )
}

@Preview
@Composable
private fun CouponDetailScreen_KidPreview() {
    CouponDetailScreen_Kid(
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
            endDate = LocalDate.now()
        ),
        onMissionClick = {},
        onRewardDeliveredClick = {},
        onRewardRemindClick = {}
    )
}

@Composable
private fun CouponDetailScreen(
    data: CouponDetailModel,
    onMissionClick: () -> Unit,
    buttons: @Composable (RowScope.() -> Unit)? = null
) = Surface(
    color = Blue500,
    modifier = Modifier.fillMaxSize()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
                    onMissionClick = onMissionClick
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
                DeliveryDayText(date = LocalDate.now())

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
}