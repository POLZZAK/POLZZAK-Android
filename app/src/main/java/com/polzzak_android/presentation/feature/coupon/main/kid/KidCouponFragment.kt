package com.polzzak_android.presentation.feature.coupon.main.kid

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentKidCouponBinding
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.coupon.main.content.CouponContentFragment

class KidCouponFragment : BaseFragment<FragmentKidCouponBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_kid_coupon

    private val previousCouponFragment = CouponContentFragment.getInstance("issued")      // todo
    private val receivedCouponFragment = CouponContentFragment.getInstance("rewarded")      // todo

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

    override fun onToolbarIconClicked() {
        // TODO: 안내 화면 표시
        Toast.makeText(requireContext(), "info click", Toast.LENGTH_SHORT).show()
    }

    override fun initView() {
        super.initView()

        replaceFragment(previousCouponFragment)
        initTabListener()
    }

    private fun initTabListener() {
        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedFragment = when (tab?.position) {
                    0 -> previousCouponFragment
                    1 -> receivedCouponFragment
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