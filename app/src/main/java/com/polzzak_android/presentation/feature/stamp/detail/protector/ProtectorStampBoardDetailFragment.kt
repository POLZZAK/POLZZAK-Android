package com.polzzak_android.presentation.feature.stamp.detail.protector

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentKidStampBoardDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogMissionData
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.common.compose.PolzzakAppTheme
import com.polzzak_android.presentation.feature.stamp.detail.screen.StampBoardDetailScreen_Kid
import com.polzzak_android.presentation.feature.stamp.detail.StampBoardDetailViewModel
import com.polzzak_android.presentation.feature.stamp.model.StampModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProtectorStampBoardDetailFragment : BaseFragment<FragmentKidStampBoardDetailBinding>() {
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
                        onStampClick = this@ProtectorStampBoardDetailFragment::openStampInfoDialog,
                        onEmptyStampClick = this@ProtectorStampBoardDetailFragment::openStampRequestDialog,
                        onRewardButtonClick = {
                            // TODO: api 나오면 구현
                        }
                    )
                }
            }
        }
    }

    private fun openStampInfoDialog(stamp: StampModel) {
        // TODO: 도장 이미지 표시
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.MISSION,
                content = CommonDialogContent(
                    title = "미션 완료",
                    mission = CommonDialogMissionData(
                        img = "",
                        missionTitle = stamp.missionContent,
                        missionTime = stamp.createdDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
                    )
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ONE,
                    positiveButtonText = "닫기"
                )
            )
        ).show(childFragmentManager, "Dialog")
    }

    private fun openStampRequestDialog() {
        // TODO: 도장 요청 모달 열기 동작 구현 -> 바텀시트 나오면
    }
}