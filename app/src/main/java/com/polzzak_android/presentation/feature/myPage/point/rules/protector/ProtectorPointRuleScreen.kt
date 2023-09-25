package com.polzzak_android.presentation.feature.myPage.point.rules.protector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.R
import com.polzzak_android.presentation.feature.myPage.components.LevelUpGuide
import com.polzzak_android.presentation.feature.myPage.components.RuleBox
import com.polzzak_android.presentation.feature.myPage.components.RuleItem

@Composable
fun ProtectorPointRuleScreen() = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
        .fillMaxSize()
        .padding(all = 16.dp)
) {
    LevelUpGuide()
    RuleBox(
        title = stringResource(id = R.string.point_rules_positive_title),
        strings = stringArrayResource(id = R.array.point_rules_protector_positive),
        itemContent = { ruleString ->
            RuleItem(iconResId = R.drawable.ic_positive_circle, text = ruleString)
        }
    )
    RuleBox(
        title = stringResource(id = R.string.point_rules_negative_title),
        strings = stringArrayResource(id = R.array.point_rules_protector_negative),
        itemContent = { ruleString ->
            RuleItem(iconResId = R.drawable.ic_negative_circle, text = ruleString)
        }
    )
}

@Preview
@Composable
private fun ProtectorPointRuleScreenPreview() {
    ProtectorPointRuleScreen()
}