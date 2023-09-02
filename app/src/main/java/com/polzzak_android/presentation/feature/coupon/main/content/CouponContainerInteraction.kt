package com.polzzak_android.presentation.feature.coupon.main.content

import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.presentation.component.SemiCircleProgressView
import com.polzzak_android.presentation.feature.coupon.model.CouponModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummary

interface CouponContainerInteraction {
    fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<CouponModel>)

    fun onCouponPagerClicked(couponModel: CouponModel)
}