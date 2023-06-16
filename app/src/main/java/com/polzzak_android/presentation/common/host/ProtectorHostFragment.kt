package com.polzzak_android.presentation.common.host

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorHostBinding
import com.polzzak_android.presentation.common.base.BaseFragment

class ProtectorHostFragment() : BaseFragment<FragmentProtectorHostBinding>() {

    override val layoutResId = R.layout.fragment_protector_host
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