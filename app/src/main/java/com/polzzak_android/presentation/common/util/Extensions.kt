package com.polzzak_android.presentation.common.util

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