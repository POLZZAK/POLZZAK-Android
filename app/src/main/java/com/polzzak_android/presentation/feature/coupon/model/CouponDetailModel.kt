package com.polzzak_android.presentation.feature.coupon.model

import com.polzzak_android.data.remote.model.response.CouponDetailDto
import com.polzzak_android.presentation.common.util.toLocalDate
import com.polzzak_android.presentation.common.util.toLocalDateTime
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

fun CouponDetailDto.toModel(): CouponDetailModel = CouponDetailModel(
    couponId = this.couponId,
    state = CouponState.getStateOrNull(this.state) ?: CouponState.ISSUED,
    rewardTitle = this.reward,
    giverName = this.guardian.nickname,
    giverProfileUrl = this.guardian.profileUrl,
    receiverName = this.kid.nickname,
    receiverProfileUrl = this.kid.profileUrl,
    missions = this.missionContents,
    stampCount = this.stampCount,
    startDate = this.startDate.toLocalDate() ?: LocalDate.now(),
    endDate =  this.endDate.toLocalDate() ?: LocalDate.now(),
    rewardRequestDate = this.rewardRequestDate.toLocalDateTime()
)
