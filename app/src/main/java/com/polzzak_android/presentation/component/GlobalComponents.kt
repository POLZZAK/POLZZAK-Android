package com.polzzak_android.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.common.compose.Blue100
import com.polzzak_android.presentation.common.compose.Blue200
import com.polzzak_android.presentation.common.compose.Blue400
import com.polzzak_android.presentation.common.compose.Blue500
import com.polzzak_android.presentation.common.compose.Blue600
import com.polzzak_android.presentation.common.compose.Blue700
import com.polzzak_android.presentation.common.compose.Gray400
import com.polzzak_android.presentation.common.compose.NoRippleTheme
import com.polzzak_android.presentation.common.compose.PolzzakTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PolzzakButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.polzzakButtonColors(),
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled = enabled)
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .height(50.dp)
                .then(modifier)
                .semantics { role = Role.Button },
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            color = colors.backgroundColor(enabled = enabled).value,
            contentColor = contentColor.copy(1f),
            border = border,
            elevation = 0.dp,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
            ProvideTextStyle(value = PolzzakTheme.typography.semiBold16) {
                Row(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(PaddingValues(horizontal = 14.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun PolzzakWhiteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    content: @Composable RowScope.() -> Unit
) = PolzzakButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colors = ButtonDefaults.polzzakButtonColors(
        backgroundColor = Color.White,
        contentColor = Blue600,
        disabledBackgroundColor = Gray400,
        disabledContentColor = Blue600
    ),
    border = border,
    content = content
)

@Composable
fun PolzzakOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) = PolzzakButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    border = BorderStroke(width = 1.dp, color = Color.White),
    content = content
)

@Composable
fun ButtonDefaults.polzzakButtonColors(
    backgroundColor: Color = Blue500,
    contentColor: Color = Color.White,
    disabledBackgroundColor: Color = Blue200,
    disabledContentColor: Color = Color.White
): ButtonColors = buttonColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor
)

@Preview
@Composable
fun ButtonPreview() {
    Column {
        PolzzakButton(onClick = { /*TODO*/ }) {
            Text(text = "확인")
        }
        PolzzakWhiteButton(onClick = { /*TODO*/ }) {
            Text(text = "확인")
        }
        PolzzakOutlineButton(onClick = { /*TODO*/ }) {
            Text(text = "확인")
        }
    }
}

/**
 * 폴짝 앱 공통 Chip
 */
@Composable
fun BlueChip(text: String) = Text(
    text = text,
    color = Color.White,
    style = PolzzakTheme.typography.semiBold16,
    modifier = Modifier
        .clip(RoundedCornerShape(corner = CornerSize(6.dp)))
        .background(color = Blue500)
        .padding(horizontal = 8.dp, vertical = 4.dp)
)

@Preview
@Composable
private fun BlueChipPreview() {
    BlueChip(text = "D+9")
}

/**
 * 알림 바
 */
@Composable
fun NoticeBar(text: String) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Blue100, shape = RoundedCornerShape(8.dp))
        .border(width = 1.dp, color = Blue700.copy(alpha = 0.16f), shape = RoundedCornerShape(8.dp))
        .padding(horizontal = 16.dp, vertical = 12.dp)
) {
    Icon(
        imageVector = Icons.Default.Notifications,
        contentDescription = text,
        tint = Blue400
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = text,
        style = PolzzakTheme.typography.semiBold14,
        color = Blue600,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
private fun NoticeBarPreview() {
    NoticeBar(text = "도장 요청이 있어요!")
}