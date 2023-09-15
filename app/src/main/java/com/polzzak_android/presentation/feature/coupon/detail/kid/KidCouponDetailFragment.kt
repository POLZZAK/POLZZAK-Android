package com.polzzak_android.presentation.feature.coupon.detail.kid

import android.widget.ImageView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.toSpannable
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.data.remote.model.ApiException
import com.polzzak_android.databinding.FragmentCouponDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.compose.PolzzakAppTheme
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
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
import com.polzzak_android.presentation.component.dialog.FullLoadingDialog
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.errorOf
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.coupon.detail.CouponDetailScreen_Kid
import com.polzzak_android.presentation.feature.coupon.detail.CouponDetailViewModel
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class KidCouponDetailFragment : BaseFragment<FragmentCouponDetailBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_detail

    private val viewModel: CouponDetailViewModel by viewModels()

    private var loadingDialog: FullLoadingDialog? = FullLoadingDialog()

    private var couponImageView: CouponImageView? = null

    override fun setToolbar() {
        super.setToolbar()

        ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "",
                iconImageId = R.drawable.ic_picture,
                iconInteraction = this@KidCouponDetailFragment
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
        val imageView = ImageView(requireContext()).apply {
            setImageBitmap(bitmap!!)
        }

        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.CAPTURE,
                content = CommonDialogContent(
                    title = "다음 쿠폰을 사진첩에 저장합니다.".toSpannable(),
                    captureView = imageView
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
                        val couponBitmap = imageView.drawToBitmap()

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

                    override fun getReturnValue(value: Any) {
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
                    CouponDetailScreen_Kid(
                        couponDetailData = viewModel.couponDetailData,
                        onMissionClick = this@KidCouponDetailFragment::openMissionsDialog,
                        onRewardRequestClick = this@KidCouponDetailFragment::requestReward,
                        onRewardDeliveredClick = this@KidCouponDetailFragment::openReceiveDialog,
                        setCouponImageView = this@KidCouponDetailFragment::setCouponView
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

    private fun requestReward(couponId: Int) {
        viewModel.requestReward(
            token = getAccessTokenOrNull() ?: "",
            couponId = couponId,
            onCompletion = { exception ->
                if (exception == null) {
                    PolzzakSnackBar.make(
                        binding.root,
                        R.string.coupon_detail_request_reward_success,
                        PolzzakSnackBar.Type.SUCCESS
                    ).show()
                } else {
                    PolzzakSnackBar.errorOf(binding.root, exception).show()
                }
            }
        )
    }

    private fun openReceiveDialog(couponId: Int, rewardTitle: String) {
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.ALERT,
                content = CommonDialogContent(
                    title = SpannableBuilder.build(requireContext()) {
                        span(
                            text = rewardTitle,
                            textColor = R.color.gray_800,
                            style = R.style.subtitle_18_600
                        )
                        span(
                            text = "\n선물을 실제로 전달 받았나요?",
                            textColor = R.color.gray_500,
                            style = R.style.body_16_500
                        )
                    }
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = "취소",
                    positiveButtonText = "네, 받았어요!"
                )
            ),
            onConfirmListener = {
                object : OnButtonClickListener {
                    override fun setBusinessLogic() {
                        receiveReward(couponId, rewardTitle)
                    }

                    override fun getReturnValue(value: Any) {
                    }
                }
            }
        ).show(childFragmentManager, null)
    }

    private fun receiveReward(couponId: Int, rewardTitle: String) {
        val dialog = CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.LOADING,
                content = CommonDialogContent(
                    title = SpannableBuilder.build(requireContext()) {
                        span(
                            text = rewardTitle,
                            textColor = R.color.gray_800,
                            style = R.style.subtitle_18_600
                        )
                        span(
                            text = "\n선물을 실제로 전달 받았나요?",
                            textColor = R.color.gray_500,
                            style = R.style.body_16_500
                        )
                    }
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ZERO,
                )
            )
        )

        viewModel
            .receiveReward(
                token = getAccessTokenOrNull() ?: "",
                couponId = couponId,
                onStart = {
                    dialog.show(childFragmentManager, null)
                },
                onCompletion = { exception ->
                    if (dialog.isVisible) {
                        dialog.dismiss()
                    }

                    if (exception != null) {
                        PolzzakSnackBar.errorOf(binding.root, exception = exception).show()
                    }
                }
            )
    }

    private fun setCouponView(view: CouponImageView) {
        couponImageView = view
    }

    override fun initObserver() {
        super.initObserver()

        // Composable에서 로딩 다이얼로그를 조작할 수 없기 때문에
        // Fragment에서 로딩과 에러 화면 처리를 합니다.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .couponDetailData
                    .collect {
                        showLoadingDialog(show = (it is ModelState.Loading))

                        if (it is ModelState.Error) {
                            handleErrorCase(exception = it.exception)
                        }
                    }
            }
        }
    }

    private fun showLoadingDialog(show: Boolean) {
        if (show) {
            if (loadingDialog == null) {
                loadingDialog = FullLoadingDialog()
            }

            loadingDialog?.show(childFragmentManager, null)
        } else {
            if (loadingDialog?.isVisible == true) {
                loadingDialog?.dismiss()
            }
            loadingDialog = null
        }
    }

    private fun handleErrorCase(exception: Exception) {
        when (exception) {
            is ApiException.TargetNotExist -> {
                CommonDialogHelper.getInstance(
                    content = CommonDialogModel(
                        type = DialogStyleType.ALERT,
                        content = CommonDialogContent(title = "쿠폰이 존재하지 않아요.".toSpannable()),
                        button = CommonButtonModel(
                            buttonCount = ButtonCount.ONE,
                            positiveButtonText = "되돌아가기",
                        )
                    ),
                    onConfirmListener = {
                        object : OnButtonClickListener {
                            override fun setBusinessLogic() {
                                findNavController().popBackStack()
                            }

                            override fun getReturnValue(value: Any) {
                            }
                        }
                    }
                ).show(childFragmentManager, null)
            }
            else -> {
                PolzzakSnackBar.errorOf(view = binding.root, exception = exception).show()
            }
        }
    }
}