package com.polzzak_android.presentation.feature.coupon.model

import com.polzzak_android.presentation.coupon.model.CouponState
import java.time.LocalDate

data class CouponDetailModel(
    val couponId: Int,
    val state: CouponState,
    val rewardTitle: String,
    val giverName: String,
    val giverProfileUrl: String,
    val receiverName: String,
    val receiverProfileUrl: String,
    val missions: List<String>,
    val stampCount: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    // TODO: 쿠폰 지급 기한 추가
)

// TODO: Dto 변환 함수 작성
