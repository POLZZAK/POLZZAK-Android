package com.polzzak_android.presentation.feature.coupon.model

import com.polzzak_android.presentation.coupon.model.CouponState
import java.time.LocalDate
import java.time.LocalDateTime

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
    val rewardRequestDate: LocalDateTime?
)

// TODO: Dto 변환 함수 작성
