package com.polzzak_android.presentation.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * 디바이스 시스템의 폰트 크기 설정에 영향 받지 않는 폰트 사이즈
 */
val Int.fixedSp: TextUnit
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp