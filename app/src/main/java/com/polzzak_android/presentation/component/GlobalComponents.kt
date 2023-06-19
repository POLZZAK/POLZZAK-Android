package com.polzzak_android.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.compose.Blue100
import com.polzzak_android.presentation.compose.Blue200
import com.polzzak_android.presentation.compose.Blue400
import com.polzzak_android.presentation.compose.Blue500
import com.polzzak_android.presentation.compose.Blue600
import com.polzzak_android.presentation.compose.Blue700
import com.polzzak_android.presentation.compose.PolzzakTheme

/**
 * 폴짝 앱 공통 버튼
 * 버튼 타입 커스텀하도록 만들기
 */
@Composable
fun PolzzakButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) = Button(
    enabled = enabled,
    onClick = onClick,
    contentPadding = PaddingValues(horizontal = 14.dp),
    shape = RoundedCornerShape(8.dp),
    colors = ButtonDefaults.buttonColors(
        backgroundColor = Blue500,
        contentColor = Color.White,
        disabledBackgroundColor = Blue200,
        disabledContentColor = Color.White
    ),
    elevation = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    ),
    modifier = Modifier
        .height(50.dp)
        .then(modifier)
) {
    Text(text = text, style = PolzzakTheme.typography.subTitle3)
}

@Preview
@Composable
private fun PolzzakButtonPreview() {
    PolzzakButton(
        text = "확인",
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * 폴짝 앱 공통 Chip
 */
@Composable
fun BlueChip(text: String) = Text(
    text = text,
    color = Color.White,
    style = PolzzakTheme.typography.subTitle3,
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
        .border(width = 1.dp, color = Blue700.copy(alpha = 0.16f))
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
        style = PolzzakTheme.typography.body2,
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