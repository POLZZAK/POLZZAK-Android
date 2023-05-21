package com.polzzak_android.presentation.main

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentMainBinding
import com.polzzak_android.presentation.main.completed.CompletedFragment
import com.polzzak_android.presentation.main.progress.ProgressFragment

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override val layoutResId: Int = R.layout.fragment_main

    private val progressFragment = ProgressFragment()
    private val completedFragment = CompletedFragment()

    override fun initView() {
        super.initView()

        childFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, progressFragment).commit()
        tabListener()
    }

    private fun tabListener() {
        binding.tabContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                var selectedFragment: Fragment? = null

                when (position) {
                    0 -> {
                        selectedFragment = progressFragment
                    }
                    1 -> {
                        selectedFragment = completedFragment
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