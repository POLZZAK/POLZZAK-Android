package com.polzzak_android.presentation.feature.stamp.main.protector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorMainBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.link.LinkViewModel
import com.polzzak_android.presentation.feature.myPage.profile.ProfileViewModel
import com.polzzak_android.presentation.feature.stamp.main.StampMainViewModel
import com.polzzak_android.presentation.feature.stamp.main.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProtectorMainFragment : BaseFragment<FragmentProtectorMainBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_protector_main

    private val stampMainViewModel by viewModels<StampMainViewModel>()

    private val protectorProgressFragment = ProtectorProgressFragment.getInstance(isKid = false)
    private val protectorCompletedFragment = ProtectorCompletedFragment.getInstance(isKid = false)

    private lateinit var toolbarHelper: ToolbarHelper

    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stampMainViewModel.requestHasNewRequest(accessToken = getAccessTokenOrNull() ?: "")
    }

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
            val hasLinkedUser = linkedUserViewModel.getLinkedUserList().isNullOrEmpty()
            if (!hasLinkedUser) {
                findNavController().navigate(R.id.action_protectorMainFragment_to_makeStampFragment)
            }
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

    override fun initObserver() {
        super.initObserver()
        stampMainViewModel.hasNewRequestLiveData.observe(viewLifecycleOwner) {
            if (::toolbarHelper.isInitialized.not()) return@observe
            val targetDrawableRes =
                if (it) R.drawable.ic_main_linked_has_request else R.drawable.ic_main_linked
            toolbarHelper.updateImageIconImage(imageResource = targetDrawableRes)
        }
    }

    override fun onToolbarIconClicked() {
        findNavController().navigate(R.id.action_protectorMainFragment_to_protectorLinkManagementFragment)
    }
}