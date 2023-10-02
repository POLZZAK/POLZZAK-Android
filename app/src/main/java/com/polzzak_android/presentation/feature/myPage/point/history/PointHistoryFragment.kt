package com.polzzak_android.presentation.feature.myPage.point.history

import android.os.Bundle
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.databinding.FragmentPointHistoryBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.handleInvalidToken
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class PointHistoryFragment : BaseFragment<FragmentPointHistoryBinding>() {
    override val layoutResId: Int = R.layout.fragment_point_history

    private val viewModel: PointHistoryViewModel by viewModels()

    override fun setToolbar() {
        super.setToolbar()

        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = getString(R.string.point_history_toolbar_title)
            ),
            toolbar = binding.toolbar
        ).set()
    }

    override fun initView() {
        super.initView()

        arguments = Bundle().apply {
            putString("token", getAccessTokenOrNull())
        }

        binding.composeView.apply {
            setContent {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                PointHistoryScreen(
                    screenStateFlow = viewModel.screenState,
                    historyListFlow = viewModel.historyListFlow,
                    onNextList = this@PointHistoryFragment::onNextList,
                    onError = this@PointHistoryFragment::onError
                )
            }
        }
    }

    private fun onNextList() {
        viewModel.getNextHistoryList(getAccessTokenOrNull() ?: "")
    }

    private fun onError(exception: Exception) {
        when (exception) {
            is ApiException.AccessTokenExpired -> {
                handleInvalidToken()
            }
            else -> {
                PolzzakSnackBar.errorOf(binding.root, exception).show()
            }
        }
    }
}