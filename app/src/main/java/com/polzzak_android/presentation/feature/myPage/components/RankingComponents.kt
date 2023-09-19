package com.polzzak_android.presentation.feature.myPage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polzzak_android.R
import com.polzzak_android.presentation.common.compose.Blue100
import com.polzzak_android.presentation.common.compose.Blue150
import com.polzzak_android.presentation.common.compose.Blue200
import com.polzzak_android.presentation.common.compose.Blue400
import com.polzzak_android.presentation.common.compose.Blue500
import com.polzzak_android.presentation.common.compose.Gray100
import com.polzzak_android.presentation.common.compose.Gray200
import com.polzzak_android.presentation.common.compose.Gray300
import com.polzzak_android.presentation.common.compose.Gray700
import com.polzzak_android.presentation.common.compose.Gray800
import com.polzzak_android.presentation.common.compose.PolzzakTheme
import com.polzzak_android.presentation.feature.myPage.model.RankingItemModel
import com.polzzak_android.presentation.feature.myPage.model.RankingStatus
import java.time.LocalDate

@Composable
fun RankingHeader(
    rankingType: String
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Blue500)
        .padding(vertical = 26.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .heightIn(max = 128.dp)
            .paint(painter = painterResource(id = R.drawable.img_confetti))
    ) {
        Text(
            text = stringResource(id = R.string.ranking_screen_title),
            color = Color.White,
            style = PolzzakTheme.typography.bold22
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.ranking_type_suffix, rankingType),
                color = Blue500,
                style = PolzzakTheme.typography.semiBold12,
                modifier = Modifier
                    .background(color = Blue100, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.ranking_top_30),
                color = Color.White,
                style = PolzzakTheme.typography.semiBold18
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "기준 시각",
                tint = Blue200,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))

            val today = LocalDate.now()
            Text(
                text = stringResource(
                    id = R.string.ranking_update_time,
                    today.monthValue,
                    today.dayOfMonth
                ),
                color = Blue200,
                style = PolzzakTheme.typography.medium12
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun RankingHeaderPreview() {
    RankingHeader(
        rankingType = "보호자"
    )
}

@Composable
fun EmptyRankingText() = Text(
    text = stringResource(id = R.string.ranking_empty_list),
    style = PolzzakTheme.typography.semiBold16,
    textAlign = TextAlign.Center,
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 16.dp)
        .background(color = Gray100, shape = RoundedCornerShape(20.dp))
        .padding(vertical = 38.dp)
)

@Preview
@Composable
fun EmptyRankingTextPreview() {
    EmptyRankingText()
}

@Composable
fun MyRankingCard(
    content: @Composable BoxScope.() -> Unit
) = Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White)
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .background(color = Blue150, shape = RoundedCornerShape(8.dp)),
    content = content
)

@Composable
fun RankingListItemBase(
    ranking: Int,
    rankingStatus: RankingStatus,
    point: Int,
    level: Int,
    profileUrl: String,
    userNickname: @Composable ColumnScope.() -> Unit
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .fillMaxWidth()
        .padding(all = 16.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(24.dp)
    ) {
        Text(
            text = "$ranking",
            color = Gray800,
            style = PolzzakTheme.typography.semiBold14
        )
        Image(
            painter = painterResource(id = rankingStatus.resId),
            contentDescription = "Ranking ${rankingStatus.name}",
            modifier = Modifier.size(8.dp)
        )
    }
    Spacer(modifier = Modifier.width(8.dp))
    // TODO: 프로필 사진으로 변경하기
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color = Blue200, shape = CircleShape)
    )
    Spacer(modifier = Modifier.width(8.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .weight(1f)
            .padding(end = 14.dp),
        content = userNickname
    )

    // 포인트 정보
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .background(
                color = Blue100,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.ranking_list_item_point_unit, point),
            color = Blue400,
            style = PolzzakTheme.typography.medium12.copy(fontSize = 10.sp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(id = R.string.ranking_list_item_level_unit, level),
            color = Blue500,
            style = PolzzakTheme.typography.semiBold12
        )
    }
}

@Preview
@Composable
private fun RankingUserItemPreview() {
    val data = RankingItemModel()
    Surface {
        RankingListItemBase(
            ranking = data.ranking,
            rankingStatus = data.rankingStatus,
            point = data.point,
            level = data.level,
            profileUrl = data.profileUrl,
            userNickname = {
                MemberTypeTag(value = "엄마")
                UserNickname(isMe = true) {
                    Text(
                        text = "가나다라마바사",
                        style = PolzzakTheme.typography.semiBold13
                    )
                }
            }
        )
    }
}

@Composable
fun UserNickname(
    isMe: Boolean,
    nickname: @Composable () -> Unit
) = Row(verticalAlignment = Alignment.CenterVertically) {
    nickname()
    Spacer(modifier = Modifier.width(8.dp))

    if (isMe) {
        MeMarker()
    }
}

@Composable
private fun MeMarker() = Text(
    text = stringResource(id = R.string.ranking_me),
    color = Color.White,
    style = PolzzakTheme.typography.medium12,
    modifier = Modifier
        .background(
            color = Blue500,
            shape = CircleShape
        )
        .padding(horizontal = 5.dp, vertical = 2.dp)
)

@Composable
fun MemberTypeTag(value: String) = Text(
    text = stringResource(id = R.string.ranking_member_type_suffix, value),
    color = Gray700,
    style = PolzzakTheme.typography.medium12,
    modifier = Modifier
        .background(color = Gray200, shape = RoundedCornerShape(4.dp))
        .border(width = 1.dp, color = Gray300, shape = RoundedCornerShape(4.dp))
        .padding(horizontal = 6.dp, vertical = 3.dp)

)

@Preview
@Composable
private fun MemberTypeTagPreview() {
    MemberTypeTag("엄마")
}