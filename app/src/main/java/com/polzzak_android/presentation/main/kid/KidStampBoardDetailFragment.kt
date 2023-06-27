package com.polzzak_android.presentation.main.kid

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentKidStampBoardDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.compose.PolzzakAppTheme
import com.polzzak_android.presentation.main.detail.StampBoardDetailScreen_Kid
import com.polzzak_android.presentation.main.detail.StampBoardDetailViewModel
import com.polzzak_android.presentation.main.model.StampModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KidStampBoardDetailFragment : BaseFragment<FragmentKidStampBoardDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_kid_stamp_board_detail

    private val viewModel: StampBoardDetailViewModel by viewModels()

    override fun initView() {
        super.initView()

        // TODO: 데이터 fetch 호출해줘야 함

        binding.composeView.apply {
            setContent {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                PolzzakAppTheme {
                    StampBoardDetailScreen_Kid(
                        stampBoardData = viewModel.stampBoardData,
                        onStampClick = this@KidStampBoardDetailFragment::openStampInfoDialog,
                        onEmptyStampClick = this@KidStampBoardDetailFragment::openStampRequestDialog,
                        onRewardButtonClick = {}
                    )
                }
            }
        }
    }

    private fun openStampInfoDialog(stamp: StampModel) {
        // TODO: 도장 정보 다이얼로그 표시 동작 구현
        // 도장 상세를 꼭 API 호출해야하나?
    }

    private fun openStampRequestDialog() {
        // TODO: 도장 요청 모달 열기 동작 구현
    }
}