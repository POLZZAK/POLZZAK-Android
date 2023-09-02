package com.polzzak_android.presentation.feature.coupon.main.protector

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentProtectorCouponBinding
import com.polzzak_android.presentation.feature.coupon.main.content.CouponContentFragment
import com.polzzak_android.presentation.feature.stamp.main.progress.ProtectorProgressFragment

class ProtectorCouponFragment : BaseFragment<FragmentProtectorCouponBinding>() {
    override val layoutResId: Int = R.layout.fragment_protector_coupon

    private val previousCouponFragment = CouponContentFragment.getInstance("previous")      // todo
    private val receivedCouponFragment = CouponContentFragment.getInstance("received")      // todo

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
}