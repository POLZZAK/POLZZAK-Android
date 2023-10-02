package com.polzzak_android.presentation.feature.onboarding.base

import android.view.View
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentOnBoardingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.shotBackPressed
import com.polzzak_android.presentation.common.util.toPx
import com.polzzak_android.presentation.feature.onboarding.OnBoardingViewModel
import com.polzzak_android.presentation.feature.onboarding.item.OnBoardingPageItem
import com.polzzak_android.presentation.feature.onboarding.model.OnBoardingPageModel

abstract class BaseOnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>() {
    override val layoutResId = R.layout.fragment_on_boarding
    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()
    abstract val pageData: List<OnBoardingPageModel>

    @get:IdRes
    abstract val actionMoveNextPage: Int

    override fun initView() {
        super.initView()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            shotBackPressed()
        }
        with(binding) {
            val adapter = BindableItemAdapter()
            vpPager.adapter = adapter
            val items = pageData.map { createOnBoardingPageItem(uiModel = it) }
            adapter.updateItem(items)
            vpPager.setCurrentItem(onBoardingViewModel.pageIndex, false)
            val betweenMarginPx = PROGRESS_BETWEEN_MARGIN_DP.toPx(binding.root.context)
            cpvProgress.betweenMarginsPx = betweenMarginPx
            cpvProgress.doOnPreDraw {
                cpvProgress.drawableWidthPx =
                    (cpvProgress.width - (betweenMarginPx * items.size - 1)) / items.size
            }
            vpPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    cpvProgress.isVisible = position < items.lastIndex
                    tvBtnStart.visibility =
                        if (position == items.lastIndex) View.VISIBLE else View.INVISIBLE
                    cpvProgress.checkedCount = position + 1
                    cpvProgress.maxCount = items.size
                }
            })
            tvBtnStart.setOnClickListener {
                findNavController().navigate(actionMoveNextPage)

            }
        }
    }

    private fun createOnBoardingPageItem(uiModel: OnBoardingPageModel): OnBoardingPageItem =
        OnBoardingPageItem(model = uiModel)


    companion object {
        private const val PROGRESS_BETWEEN_MARGIN_DP = 5
    }
}