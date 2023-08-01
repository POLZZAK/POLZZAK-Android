package com.polzzak_android.presentation.common.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.polzzak_android.R

/**
 * 커스텀으로 정의한 Typo
 */
@Immutable
data class PolzzakTypography(
    /* ---------- Title ---------- */
    val semiBold24: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    val bold22: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    val semiBold22: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    val bold20: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    /* ---------- SubTitle ---------- */
    val semiBold20: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    val regular20: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    val semiBold18: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    val regular18: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    val bold16: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val semiBold16: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    /* ---------- Body ---------- */
    val bold18: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    val medium18: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    val medium16: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    val medium15: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),
    val bold14: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    val semiBold14: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    val medium14: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    val medium13: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    /* ---------- Caption ---------- */
    val semiBold13: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    ),
    val bold12: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    val semiBold12: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),
    val medium12: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)

val PolzzakFont = FontFamily(
    Font(
        resId = R.font.pretendard_regular,  // regular 폰트는 Normal weight가 됨
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.pretendard_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.pretendard_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.pretendard_bold,
        weight = FontWeight.Bold
    ),
)

val LocalPolzzakTypography = staticCompositionLocalOf { PolzzakTypography() }