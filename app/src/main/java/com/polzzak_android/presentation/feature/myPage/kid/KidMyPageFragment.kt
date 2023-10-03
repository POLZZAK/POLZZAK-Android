package com.polzzak_android.presentation.feature.myPage.kid

import android.graphics.Paint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polzzak_android.BuildConfig
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentKidMyPageBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.checkNewestVersion
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.myPage.accountmanagement.MyAccountManagementFragment.Companion.ARGUMENT_NICKNAME_KEY
import com.polzzak_android.presentation.feature.term.TermDetailFragment
import com.polzzak_android.presentation.feature.term.model.TermType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KidMyPageFragment : BaseFragment<FragmentKidMyPageBinding>(), ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_kid_my_page

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
        setTermsLinkUnderLine()
        setVersionInfo()
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

    private fun setTermsLinkUnderLine() {
        binding.usingTerms.run {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        binding.privacyPolicy.run {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun setVersionInfo() {
        with(binding) {
            version.text = BuildConfig.VERSION_NAME
            viewLifecycleOwner.lifecycleScope.launch {
                checkNewestVersion(
                    onSuccess = { newestVersion, version ->
                        versionCheck.text =
                            getString(if (newestVersion == version) R.string.my_version_newest else R.string.my_version_need_update)
                    },
                    onFailure = {
                        versionCheck.text = "버전 확인 불가"
                        //TODO 버전 확인 불가능할 경우
                    })
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    fun onClickLinkUser() {
        // todo: 연동된 사용자 클릭
    }

    fun onClickRanking() {
        findNavController().navigate(R.id.action_kidMyPageFragment_to_kidRankingFragment)
    }

    fun onClickPointHistory() {
        findNavController().navigate(R.id.action_kidMyPageFragment_to_pointHistoryFragment)
    }

    fun onClickPointRule() {
        findNavController().navigate(R.id.action_kidMyPageFragment_to_pointRuleFragment)
    }

    fun onClickCustomerService() {
        // todo: 고객센터 클릭
    }

    fun onClickNotice() {
        findNavController().navigate(R.id.action_kidMyPageFragment_to_myNoticeFragment)
    }

    fun onClickManageAccount() {
        //TODO 닉네임 추가
        val bundle = Bundle().apply {
            putString(ARGUMENT_NICKNAME_KEY, "nickname")
        }
        findNavController().navigate(
            R.id.action_kidMyPageFragment_to_myAccountManagementFragment,
            bundle
        )
    }

    fun onClickUsingTerms() {
        val bundle = Bundle().apply {
            putParcelable(TermDetailFragment.ARGUMENT_TYPE_KEY, TermType.SERVICE)
        }
        findNavController().navigate(R.id.action_kidMyPageFragment_to_termDetailFragment, bundle)
    }

    fun onClickPrivacyPolicy() {
        val bundle = Bundle().apply {
            putParcelable(TermDetailFragment.ARGUMENT_TYPE_KEY, TermType.PRIVACY)
        }
        findNavController().navigate(R.id.action_kidMyPageFragment_to_termDetailFragment, bundle)
    }

    override fun onToolbarIconClicked() {
        // todo: 툴바 세팅 아이콘 클릭
    }
}