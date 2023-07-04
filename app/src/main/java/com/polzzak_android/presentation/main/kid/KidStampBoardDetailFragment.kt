package com.polzzak_android.presentation.main.kid

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentKidStampBoardDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.CommonDialogContent
import com.polzzak_android.presentation.common.model.CommonDialogMissionData
import com.polzzak_android.presentation.common.model.CommonDialogModel
import com.polzzak_android.presentation.common.model.DialogStyleType
import com.polzzak_android.presentation.common.widget.CommonDialogHelper
import com.polzzak_android.presentation.compose.PolzzakAppTheme
import com.polzzak_android.presentation.main.detail.StampBoardDetailScreen_Kid
import com.polzzak_android.presentation.main.detail.StampBoardDetailViewModel
import com.polzzak_android.presentation.main.model.StampModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class KidStampBoardDetailFragment : BaseFragment<FragmentKidStampBoardDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_kid_stamp_board_detail

    private val viewModel: StampBoardDetailViewModel by viewModels()

    override fun initView() {
        super.initView()

        val boardId = arguments?.getInt("boardId", -1) ?: -1
        Timber.d(">> boardId = $boardId")

        viewModel.fetchStampBoardDetailData(stampBoardId = boardId)

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