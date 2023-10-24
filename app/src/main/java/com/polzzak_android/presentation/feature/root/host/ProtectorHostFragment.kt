package com.polzzak_android.presentation.feature.root.host

import android.graphics.PorterDuff
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorHostBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.feature.root.MainActivity

class ProtectorHostFragment() : BaseFragment<FragmentProtectorHostBinding>() {

    override val layoutResId = R.layout.fragment_protector_host
    private lateinit var protectorNavController: NavController

    private val hostViewModel by viewModels<HostViewModel>()

    override fun initView() {
        super.initView()

        setupNavigationView()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
        initFragmentResultListener()
    }

    override fun onResume() {
        super.onResume()
        hostViewModel.requestHasNewNotification()
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

                    if (destination.id != R.id.protectorNotificationFragment && hostViewModel.isSelectedNotificationTab) {
                        hostViewModel.requestHasNewNotification()
                    }
                    hostViewModel.isSelectedNotificationTab =
                        (destination.id == R.id.protectorNotificationFragment)
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

    override fun initObserver() {
        super.initObserver()
        hostViewModel.hasNewNotificationLiveData.observe(viewLifecycleOwner) {
            val context = requireContext()
            val notificationMenuItem =
                binding.protectorBtmNav.menu.children.find { menuItem -> menuItem.itemId == R.id.protectorNotificationFragment }
            notificationMenuItem?.let { menuItem ->
                if (it) {
                    MenuItemCompat.setIconTintMode(menuItem, PorterDuff.Mode.DST)
                    menuItem.icon =
                        ContextCompat.getDrawable(context, R.drawable.selector_has_notification)
                } else {
                    MenuItemCompat.setIconTintMode(menuItem, null)
                    menuItem.icon =
                        ContextCompat.getDrawable(context, R.drawable.ic_menu_notification)
                }
            }
        }
    }

    private fun initFragmentResultListener() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            MainActivity.NOTIFICATION_INTENT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            binding.protectorBtmNav.selectedItemId = R.id.protectorNotificationFragment
        }
    }
}