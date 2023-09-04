package com.polzzak_android.presentation.feature.stamp.intercation

import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel
import com.polzzak_android.presentation.component.SemiCircleProgressView
import com.polzzak_android.presentation.feature.stamp.model.PartnerModel

interface MainProgressInteraction {
    fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<StampBoardSummaryModel>?, partner: PartnerModel)

    fun onStampPagerClicked(stampBoardItem: StampBoardSummaryModel, partner: PartnerModel)

    fun setProgressAnim(curCnt: Int, totalCnt: Int, view: SemiCircleProgressView)
}