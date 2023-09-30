package com.polzzak_android.data.remote.model.response

import com.polzzak_android.presentation.feature.coupon.model.Coupon
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import com.polzzak_android.presentation.feature.coupon.model.CouponPartner
import java.text.SimpleDateFormat
import java.util.Calendar

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

fun CouponListResponse.Data.toCoupon(isKid: Boolean) = Coupon(
    type = if (this.coupons.isEmpty()) 1 else 2,
    partner = this.family.toPartner(),
    couponList = this.coupons.map { it.toCouponModel(isKid) }
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

fun CouponDto.toCouponModel(isKid: Boolean) = CouponModel(
    isKid = isKid,
    id = this.couponId,
    name = this.reward,
    dDay = getRemainDay(this.rewardDate),
    deadLine = this.rewardDate
)

fun getRemainDay(rewardDay: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    val targetDate = dateFormat.parse(rewardDay)

    val today = Calendar.getInstance().time

    val differenceInMillis = targetDate.time - today.time

    return (differenceInMillis / (1000 * 60 * 60 * 24)).toString()
}