package com.polzzak_android.presentation.feature.coupon.main.protector

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentProtectorCouponBinding
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.coupon.main.content.CouponContentFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment

class ProtectorCouponFragment : BaseFragment<FragmentProtectorCouponBinding>(),
    ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_protector_coupon

    private val previousCouponFragment = CouponContentFragment.getInstance("issued", isKid =  false)      // todo
    private val receivedCouponFragment = CouponContentFragment.getInstance("rewarded", isKid =  false)      // todo

    private lateinit var toolbarHelper: ToolbarHelper

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                iconImageId = R.drawable.ic_info,
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

        childFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, previousCouponFragment).commit()
        tabListener()
    }

    private fun tabListener() {
        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                var selectedFragment: Fragment? = null

                when (position) {
                    0 -> {
                        selectedFragment = previousCouponFragment
                    }
                    1 -> {
                        selectedFragment = receivedCouponFragment
                    }
                }

                if (selectedFragment != null) {
                    childFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, selectedFragment).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onToolbarIconClicked() {
        Toast.makeText(requireContext(), "info click", Toast.LENGTH_SHORT).show()
    }
}