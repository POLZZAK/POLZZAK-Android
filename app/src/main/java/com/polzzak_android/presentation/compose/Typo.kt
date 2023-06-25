package com.polzzak_android.presentation.compose

import androidx.compose.material.Typography
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
    val title1: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    val title2: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    val title3: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    val title4: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    /* ---------- SubTitle ---------- */
    val subTitle1: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    val subTitle2: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val subTitle3: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    val subTitle4: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    val subTitle5: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    val subTitle6: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    /* ---------- Body ---------- */
    val body1: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),
    val body2: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    val body3: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    val body4: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    val body5: TextStyle  = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    val body6: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    val body7: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    val body8: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    /* ---------- Caption ---------- */
    val caption1: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),
    val caption2: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    val caption3: TextStyle = TextStyle(
        fontFamily = PolzzakFont,
        fontWeight = FontWeight.Bold,
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