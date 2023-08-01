package com.polzzak_android.presentation.feature.coupon.detail.kid

import androidx.compose.ui.platform.ViewCompositionStrategy
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentCouponDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.compose.PolzzakAppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class KidCouponDetailFragment : BaseFragment<FragmentCouponDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_coupon_detail

    override fun initView() {
        super.initView()

        val couponId = arguments?.getInt("couponId", -1) ?: -1
        Timber.d(">> couponId = $couponId")

        binding.composeView.apply {
            setContent {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                PolzzakAppTheme {
                    // TODO: [CouponDetailScreen_Kid] 호출하기
                }
            }
        }
    }

    private fun openMissionsDialog(missions: List<String>) {
        TODO("미션 리스트 표시 다이얼로그 추가 시 구현 가능")
    }
}