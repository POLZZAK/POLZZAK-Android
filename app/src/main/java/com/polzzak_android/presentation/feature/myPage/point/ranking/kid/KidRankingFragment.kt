package com.polzzak_android.presentation.feature.myPage.point.ranking.kid

import android.os.Bundle
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.databinding.FragmentPointRankingBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.handleInvalidToken
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.errorOf
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class KidRankingFragment : BaseFragment<FragmentPointRankingBinding>() {
    override val layoutResId: Int = R.layout.fragment_point_ranking

    private val viewModel: KidRankingViewModel by viewModels()

    override fun initView() {
        super.initView()

        // 해당 화면으로 넘어올 때 받는 인자가 없기 때문에
        // Bundle을 만들어서 넣어주어야 함
        arguments = Bundle().apply {
            putString("token", getAccessTokenOrNull())
        }


        binding.composeView.apply {
            setContent {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                KidRankingScreen(
                    data = viewModel.rankingScreenModel,
                    onError = this@KidRankingFragment::onError
                )
            }
        }
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