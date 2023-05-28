package com.polzzak_android.presentation.common.host

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentKidHostBinding


class KidHostFragment : BaseFragment<FragmentKidHostBinding>() {
    override val layoutResId = R.layout.fragment_kid_host

    private lateinit var navController: NavController

    override fun initView() {
        val navHost = childFragmentManager.findFragmentById(R.id.kidFcvContainer) as NavHostFragment
        navController = navHost.navController
        NavigationUI.setupWithNavController(binding.kidBtmNav, navController)


    }
}