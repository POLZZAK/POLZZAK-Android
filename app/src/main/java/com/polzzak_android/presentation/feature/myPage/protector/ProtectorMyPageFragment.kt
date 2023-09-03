package com.polzzak_android.presentation.feature.myPage.protector

import androidx.navigation.fragment.findNavController
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorMyPageBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction

class ProtectorMyPageFragment : BaseFragment<FragmentProtectorMyPageBinding>(),
    ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_protector_my_page

    private lateinit var toolbarHelper: ToolbarHelper

    override fun setToolbar() {
        super.setToolbar()

        toolbarHelper = ToolbarHelper(
            data = ToolbarData(
                iconImageId = R.drawable.ic_setting,
                iconInteraction = this
            ),
            toolbar = binding.toolbar
        )
        toolbarHelper.set()
    }

    override fun initView() {
        super.initView()
        binding.fragment = this
        setUpPointView()
    }

    private fun setUpPointView() {
        with(binding.pointRanking) {
            text = "폴짝 랭킹"
            icon.setImageResource(R.drawable.ic_point_rank)
        }

        with(binding.pointHistory) {
            text = "적립 내역"
            icon.setImageResource(R.drawable.ic_point_list)
        }

        with(binding.pointRule) {
            text = "포인트 규칙"
            icon.setImageResource(R.drawable.ic_point_rule)
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    fun onClickLinkUser() {
        // todo: 연동된 사용자 클릭
    }

    fun onClickRanking() {
        // todo: 폴짝 랭킹 클릭
    }

    fun onClickPointHistory() {
        // todo: 적립 내역 클릭
    }

    fun onClickPointRule() {
        // todo: 포인트 규칙 클릭
    }

    fun onClickCustomerService() {
        // todo: 고객센터 클릭
    }

    fun onClickNotice() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_myNoticeFragment)

    }

    fun onClickManageAccount() {
        // todo: 계정관리 클릭
    }

    fun onClickUsingTerms() {
        // todo: 이용약관 클릭
    }

    fun onClickPrivacyPolicy() {
        // todo: 개인정보처리방침 클릭
    }

    override fun onToolbarIconClicked() {
        // todo: 툴바 세팅 아이콘 클릭
    }
}