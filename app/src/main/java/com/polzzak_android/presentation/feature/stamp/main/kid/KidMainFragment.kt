package com.polzzak_android.presentation.feature.stamp.main.kid

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentKidMainBinding
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.stamp.main.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment

class KidMainFragment : BaseFragment<FragmentKidMainBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_kid_main

    private val protectorProgressFragment = ProtectorProgressFragment.getInstance()
    private val protectorCompletedFragment = ProtectorCompletedFragment.getInstance()

    private lateinit var toolbarHelper: ToolbarHelper

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                iconImageId = R.drawable.ic_main_linked,
                iconInteraction = this
            ),
            toolbar = binding.toolbar
        ).apply {
            set()
            hideBackButton()
        }
    }

    override fun onToolbarIconClicked() {
        // TODO: 연동 페이지 이동
    }

    override fun initView() {
        super.initView()

        replaceFragment(fragment = protectorProgressFragment)
        initTabListener()
    }


    private fun initTabListener() {
        binding.tabContainer.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedFragment = when (tab?.position) {
                    0 -> protectorProgressFragment
                    1 -> protectorCompletedFragment
                    else -> null
                }

                replaceFragment(fragment = selectedFragment)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun replaceFragment(fragment: Fragment?) {
        fragment ?: return

        childFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}