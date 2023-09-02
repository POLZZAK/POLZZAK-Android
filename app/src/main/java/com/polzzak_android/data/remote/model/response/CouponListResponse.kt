package com.polzzak_android.data.remote.model.response

import com.polzzak_android.presentation.feature.coupon.model.Coupon
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import com.polzzak_android.presentation.feature.coupon.model.CouponPartner

data class CouponListResponse(
    override val code: Int?,
    override val messages: List<String>?,
    override val data: List<Data>?
) : BaseResponse<List<CouponListResponse.Data>> {
    data class Data(
        val coupons: List<CouponDto>,
        val family: Family
    )
}

fun CouponListResponse.Data.toCoupon() = Coupon(
    type = if (this.coupons.isEmpty()) 1 else 2,
    partner = this.family.toPartner(),
    couponList = this.coupons.map { it.toCouponModel() }
)

data class Family(
    val memberId: Int,
    val memberType: MemberType,
    val nickname: String,
    val profileUrl: String
)

fun Family.toPartner() = CouponPartner(
    isKid = this.memberType.name == "KID",
    memberId = this.memberId,
    nickname = this.nickname,
    memberType = this.memberType.detail
)

data class MemberType(
    val detail: String,
    val name: String
)

data class CouponDto(
    val couponId: Int,
    val reward: String,
    val rewardDate: String,
    val rewardRequestDate: Any
)

fun CouponDto.toCouponModel() = CouponModel(
    id = this.couponId,
    name = this.reward,
    dDay = this.rewardDate,      // todo: 계산
    deadLine = this.rewardDate  // todo: 계산
)