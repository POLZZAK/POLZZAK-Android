package com.polzzak_android.presentation.main.protector

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentMainBinding
import com.polzzak_android.presentation.main.protector.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.main.protector.progress.ProtectorProgressFragment

class ProtectorMainFragment : BaseFragment<FragmentMainBinding>() {
    override val layoutResId: Int = R.layout.fragment_main

    private val protectorProgressFragment = ProtectorProgressFragment.getInstance()
    private val protectorCompletedFragment = ProtectorCompletedFragment.getInstance()

    override fun initView() {
        super.initView()

        childFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, protectorProgressFragment).commit()
        tabListener()
    }

    private fun tabListener() {
        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
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