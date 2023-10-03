package com.polzzak_android.presentation.feature.coupon.model

data class Coupon(
    val type: Int,
    val partner: CouponPartner,
    val couponList: List<CouponModel>
)

data class CouponPartner(
    val isKid: Boolean,
    val memberId: Int,
    val nickname: String,
    val memberType: String
)

data class CouponModel(
    val isKid: Boolean,         // 사용자
    val id: Int,                // 쿠폰 id
    val name: String,           // 쿠폰 이름(보상)
    val dDay: String,           // 쿠폰 보상 약속 기간까지 남은 일수
    val deadLine: String        // 쿠폰 보상 약속 기간
)