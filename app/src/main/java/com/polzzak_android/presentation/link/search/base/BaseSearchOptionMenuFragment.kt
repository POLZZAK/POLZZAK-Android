package com.polzzak_android.presentation.link.search.base

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchOptionMenuBinding
import com.polzzak_android.presentation.common.base.BaseFragment

abstract class BaseSearchOptionMenuFragment : BaseFragment<FragmentSearchOptionMenuBinding>() {
    override val layoutResId = R.layout.fragment_search_option_menu

    @get:DrawableRes
    abstract val iconDrawableRes: Int
    abstract val contentString: String
    abstract val searchButtonText: String

    @get:IdRes
    abstract val actionNavigateSearchFragment: Int

    @get:IdRes
    abstract val actionNavigateHostFragment: Int


    override fun initView() {
        super.initView()
        with(binding) {
            ivIcon.setBackgroundResource(iconDrawableRes)
            tvContent.text = contentString
            tvBtnSearch.text = searchButtonText
            tvBtnSearch.setOnClickListener {
                findNavController().navigate(actionNavigateSearchFragment)
            }
            tvBtnCancel.setOnClickListener {
                findNavController().navigate(actionNavigateHostFragment)
            }
        }
    }
}