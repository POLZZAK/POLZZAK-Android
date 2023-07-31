package com.polzzak_android.presentation.common.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun PolzzakAppTheme(content: @Composable () -> Unit) {
    val polzzakTypography = PolzzakTypography()

    CompositionLocalProvider(
        LocalPolzzakTypography provides polzzakTypography,
        content = {
            MaterialTheme(
                colors = LightColorPalette,
                content = content
            )
        }
    )
}

/**
 * Composable 내에서 커스텀 정의 테마에 접근할 수 있게 해주는 object.
 * ex) PolzzakTheme.typography.title1
 */
object PolzzakTheme {
    val typography: PolzzakTypography
        @Composable
        get() = LocalPolzzakTypography.current
}
