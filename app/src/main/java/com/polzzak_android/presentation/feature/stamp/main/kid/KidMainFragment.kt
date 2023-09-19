package com.polzzak_android.presentation.feature.stamp.main.kid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentKidMainBinding
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.stamp.main.StampMainViewModel
import com.polzzak_android.presentation.feature.stamp.main.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KidMainFragment : BaseFragment<FragmentKidMainBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_kid_main

    private val stampMainViewModel by viewModels<StampMainViewModel>()

    private val protectorProgressFragment = ProtectorProgressFragment.getInstance()
    private val protectorCompletedFragment = ProtectorCompletedFragment.getInstance()

    private lateinit var toolbarHelper: ToolbarHelper

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
        ).apply {
            set()
            hideBackButton()
        }
    }

    override fun onToolbarIconClicked() {
        findNavController().navigate(R.id.action_kidMainFragment_to_kidLinkManagementFragment)
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

    override fun initObserver() {
        super.initObserver()
        stampMainViewModel.hasNewRequestLiveData.observe(viewLifecycleOwner) {
            if (::toolbarHelper.isInitialized.not()) return@observe
            val targetDrawableRes =
                if (it) R.drawable.ic_main_linked_has_request else R.drawable.ic_main_linked
            toolbarHelper.updateImageIconImage(imageResource = targetDrawableRes)
        }
    }
}