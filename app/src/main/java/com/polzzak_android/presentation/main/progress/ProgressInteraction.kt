package com.polzzak_android.presentation.main.progress

import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.presentation.main.model.StampBoardSummary
import com.polzzak_android.presentation.widget.SemiCircleProgressView

interface ProgressInteraction {
    fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<StampBoardSummary>)

    fun onStampPagerClicked(stampBoardItem: StampBoardSummary)

    fun setProgressAnim(view: SemiCircleProgressView)
}