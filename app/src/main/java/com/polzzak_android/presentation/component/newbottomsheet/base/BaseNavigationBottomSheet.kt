package com.polzzak_android.presentation.component.newbottomsheet.base

import android.os.Bundle
import android.view.View
import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.polzzak_android.presentation.common.util.findNavHostFragment

/**
 * Navigation을 사용한 화면이동을 하는 바텀시트의 Base가 되는 클래스
 */
abstract class BaseNavigationBottomSheet : BaseBottomSheet() {
    @get:NavigationRes
    protected abstract val navGraphId: Int

    protected val navController: NavController by lazy {
        findNavHostFragment(baseBinding.fragmentContainer.id).findNavController()
    }

    protected abstract fun initialize()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseBinding.layoutFrame.visibility = View.GONE

        setupHostFragment()
        navController.setGraph(navGraphId)

        initialize()
    }

    private fun setupHostFragment() {
        childFragmentManager
            .beginTransaction()
            .add(baseBinding.fragmentContainer.id, NavHostFragment())
            .commitNow()
    }
}