package com.polzzak_android.presentation.main.model

/**
 * 도장판 상태 나타내는 Enum
 */
enum class StampBoardStatus {
    /**
     * 도장 모으는 중(진행 중)
     */
    PROGRESS,

    /**
     * 도장 다 모음(진행 중)
     */
    COMPLETED,

    /**
     * 쿠폰 발급(진행 중)
     */
    ISSUED_COUPON,

    /**
     * 쿠폰 수령(완료)
     */
    REWARDED
}