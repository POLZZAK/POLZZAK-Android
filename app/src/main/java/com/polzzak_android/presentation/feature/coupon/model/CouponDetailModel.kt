package com.polzzak_android.presentation.feature.coupon.model

import java.time.LocalDate

data class CouponDetailModel(
    val couponId: Int,
    val rewardTitle: String,
    val giverName: String,
    val giverProfileUrl: String,
    val receiverName: String,
    val receiverProfileUrl: String,
    val missions: List<String>,
    val stampCount: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
)

// TODO: Dto 변환 함수 작성
