package com.polzzak_android.presentation.feature.coupon.detail.kid

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.icu.util.Measure
import android.view.View.INVISIBLE
import android.view.View.MeasureSpec
import androidx.compose.ui.platform.ComposeView
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
import com.polzzak_android.presentation.feature.coupon.detail.CouponTicketImage
import com.polzzak_android.presentation.feature.stamp.model.MissionModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class KidCouponDetailFragment : BaseFragment<FragmentCouponDetailBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_detail

    private val viewModel: CouponDetailViewModel by viewModels()

    private var loadingDialog: FullLoadingDialog? = FullLoadingDialog()

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
        // TODO: 사진 저장

        // 1. 퍼미션 확인 2. 저장

        val isGranted = getPermissionManagerOrNull()?.checkPermissionAndMoveSettingIfDenied(
            PermissionManager.READ_MEDIA_PERMISSION,
            dialogTitle = getString(R.string.permission_manager_dialog_storage_title)
        ) ?: false

        if (isGranted.not()) return

        val couponData = viewModel.couponDetailData.value.data ?: return


        /*lifecycleScope.launch {
            saveBitmapToGallery(couponBitmap)
                .onSuccess {
                    PolzzakSnackBar
                        .make(binding.root, "성공!!!!", PolzzakSnackBar.Type.SUCCESS)
                        .show()
                }
                .onFailure {
                    PolzzakSnackBar
                        .make(binding.root, "실패!!!", PolzzakSnackBar.Type.WARNING)
                        .show()
                }
        }*/

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
                        onRewardDeliveredClick = viewModel::receiveReward
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
            loadingDialog?.dismiss()
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