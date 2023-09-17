package com.polzzak_android.presentation.common.util

import android.content.Context
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period


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
    return when {
        this.isAfter(nowDate.minusMonths(3)) -> "${getTimeFormat(this.monthValue)}.${
            getTimeFormat(
                this.dayOfMonth
            )
        }"

        this.isBefore(nowDate.minusYears(1)) || this.isEqual(nowDate.minusYears(1)) -> "${diff.years}년 전"
        else -> "${diff.months}개월 전"
    }
}

/**
 * 알림 날짜 표시 규칙
 * 0초~59초 => 방금 전
 * 1시간 이내는 ‘분’ 표시 => 1분 전 ~ 59분 전
 * 하루 지나기 전까지는 ‘시간’ 표시 => 1시간 ~ 23시간 전
 * 하루 지나고 3일까지는 ‘일’ 표시 => 1일 전 ~ 3일 전
 * 3일 이후부터는 월.일 표시 => ex) 05.08
 */
fun LocalDateTime.toNotificationDateString(): String {
    val nowDate = LocalDateTime.now()
    val diff = Duration.between(this, nowDate)
    return when {
        this.isAfter(nowDate.minusMinutes(1)) -> "방금 전"
        this.isAfter(nowDate.minusHours(1)) -> "${maxOf(1, diff.toMinutes())}분 전"
        this.isAfter(nowDate.minusDays(1)) -> "${maxOf(1, diff.toHours())}시간 전"
        this.isAfter(nowDate.minusDays(3)) -> "${maxOf(1, diff.toDays())}일 전"
        else -> "${getTimeFormat(this.monthValue)}.${getTimeFormat(this.dayOfMonth)}"
    }
}

private fun getTimeFormat(t: Int) = String.format("%02d", t)

/**
 * 파라미터로 받은 날짜와 며칠 차이나는지 계산합니다.
 * 0일부터 시작하지 않고 1일부터 시작합니다.
 *
 * @return 전달받은 날짜가 대상 날짜보다 이전이면 -1을 반환합니다.
 */
fun dateBetween(date1: LocalDate, date2: LocalDate): Int {
    if (date2.isBefore(date1)) {
        return -1
    }

    return Duration.between(
        date1.atStartOfDay(),
        date2.atStartOfDay()
    ).toDays()
        .toInt()
        .plus(1)
}