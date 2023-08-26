package com.polzzak_android.presentation.feature.myPage.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.Gray300
import com.polzzak_android.presentation.common.compose.PolzzakTheme

@Preview
@Composable
private fun RuleListBoxPreview() {
    Column {
        LevelUpGuide()
        RuleBox(
            title = "포인트 적립",
            strings = stringArrayResource(id = R.array.point_rules_protector_positive),
            itemContent = { ruleString ->
                RuleItem(iconResId = R.drawable.ic_positive_circle, text = ruleString)
            }
        )
    }
}

@Composable
fun LevelUpGuide() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_megaphone),
            contentDescription = "계단 상승 규칙",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "100P를 모을 때마다 1계단을 올라가요!",
            style = PolzzakTheme.typography.medium13
        )
    }
}

@Composable
fun RuleItem(
    @DrawableRes iconResId: Int,
    text: String
) = Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = "Rule icon",
        tint = Color.Unspecified,
        modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
        style = PolzzakTheme.typography.medium14,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                append(text.substringBeforeLast(" "))
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(" ${text.substringAfterLast(" ")}")
            }
        }
    )
}

@Composable
fun RuleBox(
    title: String,
    strings: Array<String>,
    itemContent: @Composable LazyItemScope.(string: String) -> Unit
) = LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        .border(width = 1.dp, color = Gray300, shape = RoundedCornerShape(8.dp))
) {
    item {
        Text(
            text = title,
            color = Color.Black,
            style = PolzzakTheme.typography.semiBold16
        )
    }

    items(
        items = strings,
        itemContent = itemContent
    )
}