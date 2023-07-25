package com.polzzak_android.presentation.feature.link.search.base

import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentSearchOptionMenuBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.shotBackPressed

abstract class BaseSearchOptionMenuFragment : BaseFragment<FragmentSearchOptionMenuBinding>() {
    override val layoutResId = R.layout.fragment_search_option_menu

    @get:DrawableRes
    abstract val iconDrawableRes: Int

    @get:StringRes
    abstract val contentStringRes: Int

    @get:StringRes
    abstract val searchButtonTextRes: Int

    @get:IdRes
    abstract val actionNavigateSearchFragment: Int

    @get:IdRes
    abstract val actionNavigateHostFragment: Int


    override fun initView() {
        super.initView()
        with(binding) {
            ivIcon.setBackgroundResource(iconDrawableRes)
            tvContent.text = binding.root.context.getString(contentStringRes)
            tvBtnSearch.text = binding.root.context.getString(searchButtonTextRes)
            tvBtnSearch.setOnClickListener {
                findNavController().navigate(actionNavigateSearchFragment)
            }
            tvBtnCancel.setOnClickListener {
                findNavController().navigate(actionNavigateHostFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback {
            shotBackPressed()
        }
    }
}