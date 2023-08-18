package com.polzzak_android.presentation.feature.coupon.detail.kid

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentCouponDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.compose.PolzzakAppTheme
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.coupon.detail.CouponDetailScreen_Kid
import com.polzzak_android.presentation.feature.coupon.detail.CouponDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class KidCouponDetailFragment : BaseFragment<FragmentCouponDetailBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_coupon_detail

    private val viewModel: CouponDetailViewModel by viewModels()

    override fun initView() {
        super.initView()

        val couponId = arguments?.getInt("couponId", -1) ?: -1
        Timber.d(">> couponId = $couponId")

        viewModel.fetchCouponDetailData(token = "", couponId = couponId)

        binding.composeView.apply {
            setContent {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                PolzzakAppTheme {
                    CouponDetailScreen_Kid(
                        couponDetailData = viewModel.couponDetailData,
                        onMissionClick = this@KidCouponDetailFragment::openMissionsDialog,
                        onRewardRequestClick = viewModel::requestReward,
                        onRewardDeliveredClick = viewModel::receiveReward
                    )
                }
            }
        }
    }

    override fun setToolbar() {
        super.setToolbar()

        val toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                popStack = findNavController(),
                titleText = "",
                iconImageId = R.drawable.ic_picture,
                iconInteraction = this@KidCouponDetailFragment
            ),
            toolbar = binding.toolbar
        ).apply { set() }

        toolbarHelper.updateToolbarBackgroundColor(R.color.primary)
        toolbarHelper.updateBackButtonColor(R.color.white)
    }

    override fun onToolbarIconClicked() {
        // TODO: 사진 저장
    }

    private fun openMissionsDialog(missions: List<String>) {
        TODO("미션 리스트 표시 다이얼로그 추가 시 구현 가능")
    }
}