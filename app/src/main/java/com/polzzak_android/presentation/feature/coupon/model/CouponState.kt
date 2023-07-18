package com.polzzak_android.presentation.coupon.model

/**
 * 쿠폰 상태를 나타내는 enum
 */
enum class CouponState {
    /**
     * 쿠폰 발급 완료 상태
     */
    ISSUED,

    /**
     * 상품 수령 완료 상태
     */
    REWARDED;

    companion object {
        fun getStateOrNull(value: String): CouponState? {
            return CouponState.values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}