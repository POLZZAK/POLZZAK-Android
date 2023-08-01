package com.polzzak_android.presentation.common.util

import android.content.Context
import android.util.DisplayMetrics
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"

fun String?.toLocalDate(): LocalDate? = kotlin.runCatching {
    LocalDate.parse(this, DateTimeFormatter.ofPattern(SERVER_DATE_FORMAT))
}.getOrNull()

fun String?.toLocalDateTime(): LocalDateTime? = kotlin.runCatching {
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(SERVER_DATE_FORMAT))
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