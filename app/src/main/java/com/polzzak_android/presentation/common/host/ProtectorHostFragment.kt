package com.polzzak_android.presentation.common.host

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentProtectortHostBinding


class ProtectorHostFragment() : BaseFragment<FragmentProtectortHostBinding>() {

    override val layoutResId = R.layout.fragment_protectort_host
    private lateinit var protectorNavController: NavController

    override fun initView() {
        super.initView()

        // set navigation
        val navHostFragment =
            childFragmentManager.findFragmentById(binding.protectorFcvContainer.id) as NavHostFragment
        protectorNavController = navHostFragment.navController

        // set bottom nav
        val btmNav = binding.protectorBtmNav
        btmNav.setupWithNavController(protectorNavController)

    }

}