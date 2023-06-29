package com.polzzak_android.presentation.onboarding.base

import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.polzzak_android.R
import com.polzzak_android.common.util.convertDp2Px
import com.polzzak_android.databinding.FragmentOnBoardingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.onboarding.OnBoardingViewModel
import com.polzzak_android.presentation.onboarding.item.OnBoardingPageItem
import com.polzzak_android.presentation.onboarding.model.OnBoardingPageModel

abstract class BaseOnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>() {
    override val layoutResId = R.layout.fragment_on_boarding
    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()
    abstract val pageData: List<OnBoardingPageModel>

    override fun initView() {
        super.initView()
        with(binding) {
            val adapter = BindableItemAdapter()
            vpPager.adapter = adapter
            val items = pageData.map { createOnBoardingPageItem(uiModel = it) }
            adapter.updateItem(items)
            val betweenMarginPx = convertDp2Px(binding.root.context, PAGE_BETWEEN_MARGIN_DP)
            cpvProgress.betweenMarginsPx = betweenMarginPx
            cpvProgress.maxCount = items.size
            cpvProgress.doOnPreDraw {
                cpvProgress.drawableWidthPx =
                    (cpvProgress.width - (betweenMarginPx * items.size - 1)) / items.size
            }
            vpPager.setCurrentItem(onBoardingViewModel.pageIndex, false)
            vpPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    cpvProgress.isVisible = (position < items.lastIndex)
                    tvBtnStart.isVisible = (position == items.lastIndex)
                    cpvProgress.checkedCount = position + 1
                }
            })
        }
    }

    private fun createOnBoardingPageItem(uiModel: OnBoardingPageModel): OnBoardingPageItem =
        OnBoardingPageItem(model = uiModel)

    companion object {
        private const val PAGE_BETWEEN_MARGIN_DP = 5
    }
}