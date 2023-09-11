package com.polzzak_android.presentation.feature.stamp.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polzzak_android.presentation.component.SkeletonView

@Composable
fun StampBoardDetailSkeleton() = Column(
    verticalArrangement = Arrangement.spacedBy(20.dp),
    modifier = Modifier.fillMaxSize()
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 16.dp)
    ) {
        // 타이틀
        SkeletonView(modifier = Modifier
            .height(34.dp)
            .fillMaxWidth(0.7f))
        // 도장판
        SkeletonView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(color = Color.White)
            .padding(top = 20.dp)
            .padding(horizontal = 16.dp)
    ) {
        // "미션" 라벨
        SkeletonView(modifier = Modifier
            .height(22.dp)
            .fillMaxWidth(0.2f))

        Spacer(modifier = Modifier.height(20.dp))

        // 미션 항목
        repeat(3) {
            SkeletonView(
                modifier = Modifier
                    .height(22.dp)
                    .fillMaxWidth(0.55f)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun StampBoardDetailSkeletonPreview() {
    StampBoardDetailSkeleton()
}