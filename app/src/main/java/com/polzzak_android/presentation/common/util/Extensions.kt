package com.polzzak_android.presentation.common.util

import android.content.Context
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter


/**
 * "yyyy-MM-dd'T'HH:mm:ss" 형태의 스트링을 [LocalDate]로 변환
 */
fun String?.toLocalDateOrNull(): LocalDate? = kotlin.runCatching {
    LocalDateTime.parse(this).toLocalDate()
}.getOrNull()

/**
 * "yyyy-MM-dd'T'HH:mm:ss" 형태의 스트링을 [LocalDateTime]로 변환
 */
fun String?.toLocalDateTimeOrNull(): LocalDateTime? = kotlin.runCatching {
    LocalDateTime.parse(this)
}.getOrNull()

fun Int.toPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return (this * metrics.density + 0.5).toInt()
}

/**
 * 전달받은 조르기 시작 시간으로부터 1시간 후까지 현재 시각 기준 몇초가 남았는지 계산
 */
fun LocalDateTime.getRemainingSeconds(): Int {
    val endTime = this.plusHours(1)
    return Duration
        .between(LocalDateTime.now(), endTime)
        .seconds
        .toInt()
}

/**
 * 날짜 규칙
 * 3달 미만의 공지: O월.O일 (ex. 06.24)
 * 3개월 이상 1년 미만의 공지: O개월 전 (ex. 3개월 전 ~ 11개월 전)
 * 1년 이상이 지난 공지: 1년 단위로 카운팅(ex. 1년 전, 2년 전)
 */
fun LocalDate.toPublishedDateString(): String {
    val nowDate = LocalDate.now()
    val diff = Period.between(this, nowDate)
    val getDateStr = { date: Int -> String.format("%02d", date) }
    return when {
        this.isAfter(nowDate.minusMonths(3)) -> "${getDateStr(this.monthValue)}.${getDateStr(this.dayOfMonth)}"
        this.isBefore(nowDate.minusYears(1)) || this.isEqual(nowDate.minusYears(1)) -> "${diff.years}년 전"
        else -> "${diff.months}개월 전"
    }
}

