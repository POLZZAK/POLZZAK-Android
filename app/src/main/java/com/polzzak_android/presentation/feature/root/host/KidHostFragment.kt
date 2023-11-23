package com.polzzak_android.presentation.feature.root.host

import android.graphics.PorterDuff
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.polzzak_android.R
import com.polzzak_android.common.util.livedata.EventWrapperObserver
import com.polzzak_android.databinding.FragmentKidHostBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.shotBackPressed
import com.polzzak_android.presentation.feature.root.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KidHostFragment : BaseFragment<FragmentKidHostBinding>() {
    override val layoutResId = R.layout.fragment_kid_host

    private lateinit var navController: NavController

    private val hostViewModel by viewModels<HostViewModel>()

    private val mainViewModel by activityViewModels<MainViewModel>()

    // 시스템 back 버튼 동작 가로치개 위한 callback
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack()
        }
    }

    private val finishCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            shotBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        hostViewModel.requestHasNewNotification(accessToken = getAccessTokenOrNull())
    }

    override fun initView() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, finishCallback)
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
                    finishCallback.isEnabled = true
                    binding.kidBtmNav.visibility = View.VISIBLE

                    if (destination.id != R.id.kidNotificationFragment && hostViewModel.isSelectedNotificationTab) {
                        hostViewModel.requestHasNewNotification(
                            accessToken = getAccessTokenOrNull()
                        )
                    }
                    hostViewModel.isSelectedNotificationTab =
                        (destination.id == R.id.kidNotificationFragment)
                }

                else -> {
                    finishCallback.isEnabled = false
                    backPressedCallback.isEnabled = true
                    binding.kidBtmNav.visibility = View.GONE
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        hostViewModel.hasNewNotificationLiveData.observe(viewLifecycleOwner) {
            val context = requireContext()
            val notificationMenuItem =
                binding.kidBtmNav.menu.children.find { menuItem -> menuItem.itemId == R.id.kid_notification_nav_graph }
            notificationMenuItem?.let { menuItem ->
                if (it) {
                    MenuItemCompat.setIconTintMode(menuItem, PorterDuff.Mode.DST)
                    menuItem.icon =
                        ContextCompat.getDrawable(context, R.drawable.selector_has_notification)
                } else {
                    MenuItemCompat.setIconTintMode(menuItem, PorterDuff.Mode.SRC_IN)
                    menuItem.icon =
                        ContextCompat.getDrawable(context, R.drawable.ic_menu_notification)
                }
            }
        }

        mainViewModel.moveNotificationTabLiveEvent.observe(
            viewLifecycleOwner,
            EventWrapperObserver {
                if (binding.kidBtmNav.selectedItemId == R.id.kid_notification_nav_graph) mainViewModel.refreshNotifications()
                else binding.kidBtmNav.selectedItemId = R.id.kid_notification_nav_graph
            })
    }
}