package com.polzzak_android.presentation.main

import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentStampDetailBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.compose.PolzzakAppTheme

/**
 * 임시 도장판 상세 화면용 Fragment
 */
class DetailFragment : BaseFragment<FragmentStampDetailBinding>() {
    override val layoutResId: Int = R.layout.fragment_stamp_detail

    override fun initView() {
        super.initView()

        binding.composeView.apply {
            setContent {
                PolzzakAppTheme {
                    StampBoardDetailScreen()
                }
            }
        }
    }
}
