package com.polzzak_android.presentation.feature.root.host

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorHostBinding
import com.polzzak_android.presentation.common.base.BaseFragment

class ProtectorHostFragment() : BaseFragment<FragmentProtectorHostBinding>() {

    override val layoutResId = R.layout.fragment_protector_host
    private lateinit var protectorNavController: NavController

    override fun initView() {
        super.initView()

        setupNavigationView()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun setupNavigationView() {
        // set navigation
        val navHostFragment =
            childFragmentManager.findFragmentById(binding.protectorFcvContainer.id) as NavHostFragment
        protectorNavController = navHostFragment.navController

        // set bottom nav
        val btmNav = binding.protectorBtmNav
        btmNav.setupWithNavController(protectorNavController)

        // bottom nav visibility control
        protectorNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.protectorMainFragment, R.id.protectorCouponFragment, R.id.protectorNotificationFragment, R.id.protectorMyPageFragment -> {
                    btmNav.visibility = View.VISIBLE
                }
                else -> {
                    btmNav.visibility = View.GONE
                }
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            protectorNavController.popBackStack()
        }
    }

}