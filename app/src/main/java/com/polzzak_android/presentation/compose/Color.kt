package com.polzzak_android.presentation.compose

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Blue100 = Color(0xFFF0F7FF)
val Blue150 = Color(0xFFE3F2FF)
val Blue200 = Color(0xFFC7E5FF)
val Blue400 = Color(0xFF84CCFF)
val Blue500 = Color(0xFF59B9FF)     // primary
val Blue600 = Color(0xFF259BEF)     // highlight
val Blue700 = Color(0xFF0D7AD3)

val Gray100 = Color(0xFFF8F8FC)
val Gray200 = Color(0xFFEEEEF4)
val Gray300 = Color(0xFFDADAE7)
val Gray400 = Color(0xFFC5C6D0)
val Gray500 = Color(0xFF9C9CA8)
val Gray600 = Color(0xFF6B6C76)
val Gray700 = Color(0xFF45464F)
val Gray800 = Color(0xFF2E3038)

val Error100 = Color(0xFFFFEEEA)
val Error200 = Color(0xFFFFDAD1)
val Error500 = Color(0xFFFF6F50)

// TODO: 색깔도 커스텀 클래스 만들어서 PolzzakTheme으로 접근할 수 있게 해야할듯? -> 상황별 정의가 필요해짐

/**
 * MaterialTheme에 제공할 컬러 팔레트
 */
val LightColorPalette = lightColors(
    primary = Blue500,
    primaryVariant = Blue600,
    background = Gray100,
    surface = Color.White,
    error = Error500,
    onPrimary = Color.White,
    onBackground = Gray800,
    onSurface = Gray800,
    onError = Color.White
)
