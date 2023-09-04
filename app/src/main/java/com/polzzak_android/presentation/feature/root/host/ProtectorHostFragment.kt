package com.polzzak_android.presentation.feature.root.host

import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorHostBinding
import com.polzzak_android.presentation.common.base.BaseFragment

class ProtectorHostFragment() : BaseFragment<FragmentProtectorHostBinding>(),
    RootNavigationController {

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

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            protectorNavController.popBackStack()
        }
    }

    override fun backToTheLoginFragment() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }
}