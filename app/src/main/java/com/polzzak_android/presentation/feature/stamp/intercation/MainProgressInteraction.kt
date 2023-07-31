package com.polzzak_android.presentation.feature.stamp.intercation

import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummary
import com.polzzak_android.presentation.component.SemiCircleProgressView

interface MainProgressInteraction {
    fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<StampBoardSummary>)

    fun onStampPagerClicked(stampBoardItem: StampBoardSummary)

    fun setProgressAnim(curCnt: Int, totalCnt: Int, view: SemiCircleProgressView)
}