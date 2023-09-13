package com.polzzak_android.presentation.feature.stamp.intercation

import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel
import com.polzzak_android.presentation.component.SemiCircleProgressView

interface MainCompletedInteraction {
    fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<StampBoardSummaryModel>?)
}