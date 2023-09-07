package com.polzzak_android.presentation.feature.stamp.detail.kid

import androidx.annotation.DrawableRes
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.toSpannable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.dialog.FullLoadingDialog
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.feature.stamp.detail.screen.StampBoardDetailScreen_Kid
import com.polzzak_android.presentation.feature.stamp.detail.StampBoardDetailViewModel
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import com.polzzak_android.presentation.feature.stamp.model.StampModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class KidStampBoardDetailFragment : BaseFragment<FragmentKidStampBoardDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_kid_stamp_board_detail

    private val viewModel: StampBoardDetailViewModel by viewModels()

    private lateinit var toolbarHelper: ToolbarHelper

    private val loadingDialog: FullLoadingDialog by lazy {
        FullLoadingDialog()
    }

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(popStack = findNavController()),
            toolbar = binding.toolbar
        ).apply {
            set()
            updateToolbarBackgroundColor(R.color.gray_100)
        }
    }

    override fun initView() {
        super.initView()

        arguments?.putString("token", getAccessTokenOrNull())

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
                        onRewardButtonClick = {
                            // TODO: api 나오면 구현
                        }
                    )
                }
            }
        }
    }

    /**
     * 도장 정보 표시하는 다이얼로그 표시.
     */
    private fun openStampInfoDialog(stamp: StampModel) {
        // TODO: 도장 이미지 표시
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.MISSION,
                content = CommonDialogContent(
                    title = "미션 완료".toSpannable(),
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

    /**
     * 도장 요청 다이얼로그 표시.
     */
    private fun openStampRequestDialog() = viewModel.stampBoardData.value.data?.also {
        val missionList = it.missionList

        val bottomSheet = CommonBottomSheetHelper.getInstance(
            data = CommonBottomSheetModel(
                type = BottomSheetType.MISSION,
                title = "도장 요청 보내기".toSpannable(),
                subTitle = "어떤 미션을 완료했나요?".toSpannable(),
                contentList = missionList,
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = "요청 취소",
                    positiveButtonText = "요청하기"
                )
            ),
            onClickListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                    }

                    override fun getReturnValue(value: Any) {
                        // 파라미터로 선택된 MissionModel이 옴
                        val missionId = (value as MissionModel).id
                        requestStamp(missionId)
                    }
                }
            }
        )

        bottomSheet.show(childFragmentManager, null)
    }

    /**
     * 보호자에게 도장 요청. 요청의 결과 받을 때 까지 로딩 화면 표시.
     */
    private fun requestStamp(missionId: Int) {
        viewModel.requestStampToProtector(
            accessToken = getAccessTokenOrNull() ?: "",
            missionId = missionId,
            onStart = {
                loadingDialog.message = "도장 요청 중"
                loadingDialog.show(childFragmentManager, null)
            },
            onCompletion = { exception ->
                loadingDialog.dismiss()

                if (exception == null) {
                    // 성공
                    openSuccessDialog(stampImageId = null) {
                        span(
                            text = "${viewModel.partnerType}에게\n",
                            style = R.style.subtitle_18_600,
                            textColor = R.color.primary_600
                        )
                        span(
                            text = "도장을 요청했어요!",
                            style = R.style.subtitle_16_600,
                            textColor = R.color.gray_800
                        )
                    }
                } else {
                    // 실패
                    exception.printStackTrace()
                    // TODO: 바텀시트에 미션 미리 선택할 수 있는 기능 추가되면 구현하기
                }
            }
        )
    }

    /**
     * 요청 성공 다이얼로그 표시
     */
    private fun openSuccessDialog(
        @DrawableRes stampImageId: Int? = null,
        titleText: SpannableBuilder.() -> Unit
    ) {
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.STAMP,
                content = CommonDialogContent(
                    title = SpannableBuilder.build(
                        context = requireContext(),
                        block = titleText
                    ),
                    stampImg = stampImageId ?: R.drawable.ic_setting    // TODO: 임시 null 처리
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ONE,
                    positiveButtonText = "닫기"
                )
            )
        ).show(childFragmentManager, null)
    }
}