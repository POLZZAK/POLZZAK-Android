package com.polzzak_android.presentation.main.model

import java.time.LocalDateTime

/**
 * 도장판 상세에서 찍힌 도장에 대한 정보를 가지는 데이터 클래스
 */
data class StampModel(
    val id: Int = 0,
    val stampDesignId: Int = 0,
    val missionContent: String = "",
    val createdDate: LocalDateTime = LocalDateTime.now()
)
