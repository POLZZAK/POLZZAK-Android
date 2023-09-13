package com.polzzak_android.presentation.feature.stamp.main.protector

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorMainBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.stamp.main.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment

class ProtectorMainFragment : BaseFragment<FragmentProtectorMainBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_protector_main

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
        )

        with(toolbarHelper) {
            set()
            hideBackButton()
        }
    }

    override fun initView() {
        super.initView()

        childFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, protectorProgressFragment).commit()
        tabListener()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_protectorMainFragment_to_makeStampFragment)
        }
    }

    private fun tabListener() {
        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                var selectedFragment: Fragment? = null

                when (position) {
                    0 -> {
                        selectedFragment = protectorProgressFragment
                    }

                    1 -> {
                        selectedFragment = protectorCompletedFragment
                    }
                }

                if (selectedFragment != null) {
                    childFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, selectedFragment).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onToolbarIconClicked() {
        findNavController().navigate(R.id.action_protectorMainFragment_to_protectorLinkManagementFragment)
    }
}