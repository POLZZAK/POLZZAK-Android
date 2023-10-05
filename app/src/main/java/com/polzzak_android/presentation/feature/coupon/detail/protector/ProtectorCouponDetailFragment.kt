package com.polzzak_android.presentation.feature.coupon.detail.protector

import android.graphics.Bitmap
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.toSpannable
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentCouponDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.compose.PolzzakAppTheme
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.util.PermissionManager
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.getPermissionManagerOrNull
import com.polzzak_android.presentation.common.util.saveBitmapToGallery
import com.polzzak_android.presentation.component.CouponImageView
import com.polzzak_android.presentation.component.PolzzakSnackBar
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.coupon.detail.CouponDetailScreen_Protector
import com.polzzak_android.presentation.feature.coupon.detail.CouponDetailViewModel
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import kotlinx.coroutines.launch

class ProtectorCouponDetailFragment : BaseFragment<FragmentCouponDetailBinding>(),
    ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_detail

    private val viewModel: CouponDetailViewModel by viewModels()

    private var couponImageView: CouponImageView? = null

    override fun setToolbar() {
        super.setToolbar()

        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "",
                iconImageId = R.drawable.ic_picture,
                iconInteraction = this@ProtectorCouponDetailFragment
            ),
            toolbar = binding.toolbar
        ).apply {
            set()
            updateBackButtonColor(R.color.white)
            updateToolbarBackgroundColor(R.color.primary)
        }
    }

    override fun onToolbarIconClicked() {
        // 권환 확인
        val isGranted = getPermissionManagerOrNull()?.checkPermissionAndMoveSettingIfDenied(
            PermissionManager.READ_MEDIA_PERMISSION,
            dialogTitle = getString(R.string.permission_manager_dialog_storage_title)
        ) ?: false

        // 권한 없으면 리턴
        if (isGranted.not()) return

        // 쿠폰 이미지 뷰가 null이면 리턴
        couponImageView ?: return

        // 화면에 나타난 쿠폰 View를 비트맵으로 변환
        val bitmap = couponImageView?.drawToBitmap()

        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.CAPTURE,
                content = CommonDialogContent(
                    title = "다음 쿠폰을 사진첩에 저장합니다.".toSpannable(),
                    captureBitmap = bitmap
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    positiveButtonText = "저장하기",
                    negativeButtonText = "취소"
                )
            ),
            onConfirmListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                    }

                    override fun getReturnValue(value: Any) {
                        val couponBitmap = value as Bitmap

                        lifecycleScope.launch {
                            saveBitmapToGallery(couponBitmap)
                                .onSuccess {
                                    PolzzakSnackBar
                                        .make(binding.root, "쿠폰이 사진첩에 저장됐어요", PolzzakSnackBar.Type.SUCCESS)
                                        .show()
                                }
                                .onFailure {
                                    PolzzakSnackBar
                                        .make(binding.root, "저장에 실패했습니다", PolzzakSnackBar.Type.WARNING)
                                        .show()
                                }
                        }
                    }
                }
            }
        ).show(childFragmentManager, null)
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
                    CouponDetailScreen_Protector(
                        couponDetailData = viewModel.couponDetailData,
                        onMissionClick = this@ProtectorCouponDetailFragment::openMissionsDialog,
                        setCouponImageView = this@ProtectorCouponDetailFragment::setCouponView
                    )
                }
            }
        }
    }

    private fun openMissionsDialog(missions: List<String>) {
        val missionList = missions.mapIndexed { index, s ->
            MissionModel(id = index, content = s)
        }

        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.MISSION_LIST,
                content = CommonDialogContent(
                    title = SpannableBuilder.build(requireContext()) {
                        span(text = "완료한 미션", textColor = R.color.gray_800)
                        span(text = "${missionList.size}", textColor = R.color.primary_600)
                    },
                    missionList = missionList
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ONE,
                    positiveButtonText = "닫기"
                )
            )
        ).show(childFragmentManager, null)
    }

    private fun setCouponView(view: CouponImageView) {
        couponImageView = view
    }

}