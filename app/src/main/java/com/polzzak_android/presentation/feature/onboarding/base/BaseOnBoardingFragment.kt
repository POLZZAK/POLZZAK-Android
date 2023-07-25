package com.polzzak_android.presentation.feature.onboarding.base

import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentOnBoardingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.shotBackPressed
import com.polzzak_android.presentation.feature.onboarding.OnBoardingClickListener
import com.polzzak_android.presentation.feature.onboarding.OnBoardingViewModel
import com.polzzak_android.presentation.feature.onboarding.item.OnBoardingPageItem
import com.polzzak_android.presentation.feature.onboarding.model.OnBoardingPageModel

abstract class BaseOnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(),
    OnBoardingClickListener {
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
        }
    }

    private fun createOnBoardingPageItem(uiModel: OnBoardingPageModel): OnBoardingPageItem =
        OnBoardingPageItem(model = uiModel, clickListener = this@BaseOnBoardingFragment)

    override fun onNext() {
        findNavController().navigate(actionMoveNextPage)
    }
}