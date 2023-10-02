package com.polzzak_android.presentation.feature.myPage.point.rules

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentPointRuleBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.myPage.point.rules.kid.KidPointRuleScreen
import com.polzzak_android.presentation.feature.myPage.point.rules.protector.ProtectorPointRuleScreen

class PointRuleFragment : BaseFragment<FragmentPointRuleBinding>() {
    override val layoutResId: Int = R.layout.fragment_point_rule

    override fun setToolbar() {
        super.setToolbar()

        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = getString(R.string.point_rules_toolbar_title)
            ),
            toolbar = binding.toolbar
        ).set()
    }

    override fun initView() {
        super.initView()

        val isKid = arguments?.getBoolean("isKid") ?: false

        binding.composeView.apply {
            setContent {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                if (isKid) {
                    KidPointRuleScreen()
                } else {
                    ProtectorPointRuleScreen()
                }
            }
        }
    }
}