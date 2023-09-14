package com.polzzak_android.presentation.feature.root.host

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentKidHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KidHostFragment : BaseFragment<FragmentKidHostBinding>(), RootNavigationOwner {
    override val layoutResId = R.layout.fragment_kid_host

    private lateinit var navController: NavController

    // 시스템 back 버튼 동작 가로치개 위한 callback
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack()
        }
    }

    override fun initView() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)

        setupNavigationView()
    }

    private fun setupNavigationView() {
        val navHost = childFragmentManager.findFragmentById(R.id.kidFcvContainer) as NavHostFragment
        navController = navHost.navController
        NavigationUI.setupWithNavController(binding.kidBtmNav, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                destination.parent?.findStartDestination()?.id -> {
                    backPressedCallback.isEnabled = false
                    binding.kidBtmNav.visibility = View.VISIBLE
                }
                else -> {
                    backPressedCallback.isEnabled = true
                    binding.kidBtmNav.visibility = View.GONE
                }
            }
        }
    }

    override fun backToTheLoginFragment() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }
}