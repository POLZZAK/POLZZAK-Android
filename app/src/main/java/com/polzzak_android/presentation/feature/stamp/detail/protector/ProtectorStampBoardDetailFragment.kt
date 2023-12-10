package com.polzzak_android.presentation.feature.stamp.detail.protector

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.toSpannable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.databinding.FragmentKidStampBoardDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.compose.PolzzakAppTheme
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogMissionData
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.FullLoadingDialog
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.newbottomsheet.makestamp.MakeStampBottomSheet
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.stamp.detail.StampBoardDetailViewModel
import com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.StampBottomSheetViewModel
import com.polzzak_android.presentation.feature.stamp.detail.screen.StampBoardDetailScreen_Kid
import com.polzzak_android.presentation.feature.stamp.detail.screen.StampBoardDetailScreen_Protector
import com.polzzak_android.presentation.feature.stamp.model.MissionRequestModel
import com.polzzak_android.presentation.feature.stamp.model.StampIcon
import com.polzzak_android.presentation.feature.stamp.model.StampModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProtectorStampBoardDetailFragment : BaseFragment<FragmentKidStampBoardDetailBinding>(),
    ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_kid_stamp_board_detail

    private val viewModel: StampBoardDetailViewModel by viewModels()

    private lateinit var toolbarHelper: ToolbarHelper

    private val loadingDialog: FullLoadingDialog by lazy {
        FullLoadingDialog()
    }
    private var dialog: DialogFragment? = null

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                iconImageId = R.drawable.ic_pencil,
                iconInteraction = this
            ),
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
                    StampBoardDetailScreen_Protector(
                        stampBoardData = viewModel.stampBoardData,
                        onStampRequestClick = this@ProtectorStampBoardDetailFragment::openMakeRequestStampBottomSheet,
                        onStampClick = this@ProtectorStampBoardDetailFragment::openStampInfoDialog,
                        onEmptyStampClick = this@ProtectorStampBoardDetailFragment::openMakeStampBottomSheet,
                        onRewardButtonClick = { /*TODO*/ },
                        onBoardDeleteClick = { /*TODO*/ },
                        onError = this@ProtectorStampBoardDetailFragment::handleErrorCase
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
                        img = StampIcon.values()[stamp.stampDesignId].resId,
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
     * 미션 요청 있을 때의 도장 찍기 바텀시트 표시
     */
    private fun openMakeRequestStampBottomSheet(requestMissionList: List<MissionRequestModel>) {
        MakeStampBottomSheet(
            missionList = requestMissionList,
            onMakeStampClick = { missionId, stampDesignId ->
                makeStamp(
                    missionId = null,
                    missionRequestId = missionId,
                    stampDesignId = stampDesignId
                )
            }
        ).show(childFragmentManager, null)
    }

    /**
     * 미션 직접 선택하는 도장 찍기 바텀시트 표시
     */
    private fun openMakeStampBottomSheet() = viewModel.stampBoardData.value.data?.also{
        MakeStampBottomSheet(
            missionList = it.missionList,
            onMakeStampClick = { missionId, stampDesignId ->
                makeStamp(
                    missionId = missionId,
                    missionRequestId = null,
                    stampDesignId = stampDesignId
                )
            }
        ).show(childFragmentManager, null)
    }

    /**
     * 도장 찍기 Api 호출
     *
     * @param missionId 미션 직접 선택 시의 미션 id
     * @param missionRequestId 요청된 미션 선택 시의 미션 id
     */
    private fun makeStamp(missionId: Int?, missionRequestId: Int?, stampDesignId: Int) {
        viewModel.makeStamp(
            token = getAccessTokenOrNull() ?: "",
            missionId = missionId,
            missionRequestId = missionRequestId,
            stampDesignId = stampDesignId,
            onStart = {
                loadingDialog.message = "도장 찍는 중"
                showDialog(newDialog = loadingDialog)
            },
            onCompletion = {
                dismissDialog()

                if (it != null) {
                    PolzzakSnackBar.errorOf(binding.root, it).show()
                } else {
                    val stampIcon = StampIcon.values()[stampDesignId]

                    openSuccessDialog(
                        stampImageId = stampIcon.resId,
                        titleText = {
                            span(
                                text = "${stampIcon.title}\n",
                                style = R.style.subtitle_18_600,
                                textColor = R.color.primary_600
                            )
                            span(
                                text = "도장이 찍혔어요!",
                                style = R.style.subtitle_16_600,
                                textColor = R.color.gray_800
                            )
                        }
                    )

                    viewModel.fetchStampBoardDetailData(
                        accessToken = getAccessTokenOrNull() ?: "",
                        stampBoardId = viewModel.stampBoardId
                    )
                }
            }
        )
    }

    /**
     * 보상(쿠폰) 받는 BottomSheet 표시
     */
    private fun openRewardSheet() {
        CommonBottomSheetHelper.getInstance(
            data = CommonBottomSheetModel(
                type = BottomSheetType.COUPON,
                title = SpannableBuilder.build(requireContext()) {
                    span(
                        text = viewModel.stampBoardData.value.data?.rewardTitle ?: "",
                        style = R.style.subtitle_18_600,
                        textColor = R.color.primary_600
                    )
                    span(
                        text = "쿠폰을 선물 받았어요!",
                        style = R.style.subtitle_16_600
                    )
                },
                contentList = listOf(
                    viewModel
                        .stampBoardData
                        .value
                        .data
                        ?.rewardDate
                        ?.format(
                            DateTimeFormatter.ofPattern("yyyy.MM.dd")
                        )
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ONE,
                    positiveButtonText = "쿠폰 받기"
                )
            ),
            onClickListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                    }

                    override fun getReturnValue(value: Any) {
                        receiveCoupon()
                    }
                }
            }
        ).show(childFragmentManager, null)
    }

    /**
     * 쿠폰 받기 API 호출
     */
    private fun receiveCoupon() {
        viewModel.receiveCoupon(
            accessToken = getAccessTokenOrNull() ?: "",
            onStart = {
                loadingDialog.message = "쿠폰 받는 중"
                loadingDialog.show(childFragmentManager, null)
            },
            onCompletion = { exception ->
                loadingDialog.dismiss()

                if (exception == null) {
                    // 성공
                    openSuccessDialog(
                        stampImageId = null,
                        titleText = {
                            span(
                                text = "${viewModel.stampBoardData.value.data?.rewardTitle}\n",
                                style = R.style.subtitle_18_600,
                                textColor = R.color.primary_600
                            )
                            span(
                                text = "쿠폰 받기 완료!",
                                style = R.style.subtitle_16_600,
                                textColor = R.color.gray_800
                            )
                        }
                    )

                    // TODO: 쿠폰 수령 후 데이터 새로 받아와서 쿠폰 받기 버튼 비활성화 되는지 테스트
                    viewModel.fetchStampBoardDetailData(
                        accessToken = getAccessTokenOrNull() ?: "",
                        stampBoardId = viewModel.stampBoardId
                    )
                } else {
                    // 실패
                    PolzzakSnackBar.errorOf(view = binding.root, exception = exception).show()
                    openRewardSheet()
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

    /**
     * Exception 종류에 따라 표시할 에러 화면 구분하여 표시
     */
    private fun handleErrorCase(exception: Exception) {
        when (exception) {
            is ApiException.TargetNotExist -> {
                CommonDialogHelper.getInstance(
                    content = CommonDialogModel(
                        type = DialogStyleType.ALERT,
                        content = CommonDialogContent(title = "도장판이 존재하지 않아요.".toSpannable()),
                        button = CommonButtonModel(
                            buttonCount = ButtonCount.ONE,
                            positiveButtonText = "되돌아가기"
                        )
                    )
                ).show(childFragmentManager, null)
            }
            else -> {
                PolzzakSnackBar.errorOf(view = binding.root, exception = exception)
            }
        }
    }

    override fun onToolbarIconClicked() {
        findNavController().navigate(resId = R.id.action_protectorStampBoardDetailFragment_to_makeStampFragment,
            args = Bundle().apply {
                putInt("partnerId", SavedStateHandle().get<Int>("partnerId") ?: -1)
                putInt("boardId", viewModel.stampBoardId)
            })
    }

    private fun showDialog(newDialog: DialogFragment) {
        dismissDialog()
        dialog = newDialog
        dialog?.show(childFragmentManager, null)
    }

    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }
}