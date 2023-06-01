package com.polzzak_android.presentation.main.kid

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentKidMainBinding
import com.polzzak_android.presentation.main.protector.completed.ProtectorCompletedFragment
import com.polzzak_android.presentation.main.protector.progress.ProtectorProgressFragment

class KidMainFragment : BaseFragment<FragmentKidMainBinding>() {
    override val layoutResId: Int = R.layout.fragment_kid_main

    private val protectorProgressFragment = ProtectorProgressFragment.getInstance()
    private val protectorCompletedFragment = ProtectorCompletedFragment.getInstance()

    override fun initView() {
        super.initView()

        replaceFragment(fragment = protectorProgressFragment)
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