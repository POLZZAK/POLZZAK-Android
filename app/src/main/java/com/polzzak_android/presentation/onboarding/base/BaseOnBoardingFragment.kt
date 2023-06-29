package com.polzzak_android.presentation.onboarding.base

import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.polzzak_android.R
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
    private val pageCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onBoardingViewModel.pageIndex = position
        }
    }

    override fun initView() {
        super.initView()
        with(binding) {
            val adapter = BindableItemAdapter()
            vpPager.adapter = adapter
            val items = pageData.map { createOnBoardingPageItem(uiModel = it) }
            adapter.updateItem(items)
            vpPager.setCurrentItem(onBoardingViewModel.pageIndex, false)
            vpPager.registerOnPageChangeCallback(pageCallback)
        }
    }

    private fun createOnBoardingPageItem(uiModel: OnBoardingPageModel): OnBoardingPageItem =
        OnBoardingPageItem(model = uiModel)

    override fun onDestroyView() {
        binding.vpPager.unregisterOnPageChangeCallback(pageCallback)
        super.onDestroyView()
    }
}