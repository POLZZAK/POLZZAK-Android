package com.polzzak_android.presentation.feature.myPage.protector

import android.graphics.Paint
import android.os.Bundle
import androidx.core.text.toSpannable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.polzzak_android.BuildConfig
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProtectorMyPageBinding
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.SpannableBuilder
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.getInAppUpdateCheckerOrNull
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.toolbar.ToolbarData
import com.polzzak_android.presentation.component.toolbar.ToolbarHelper
import com.polzzak_android.presentation.component.toolbar.ToolbarIconInteraction
import com.polzzak_android.presentation.feature.myPage.accountmanagement.MyAccountManagementFragment
import com.polzzak_android.presentation.feature.myPage.model.LevelModel
import com.polzzak_android.presentation.feature.myPage.profile.ProfileViewModel
import com.polzzak_android.presentation.feature.stamp.main.protector.StampLinkedUserViewModel
import com.polzzak_android.presentation.feature.term.TermDetailFragment
import com.polzzak_android.presentation.feature.term.model.TermType
import kotlinx.coroutines.launch

class ProtectorMyPageFragment : BaseFragment<FragmentProtectorMyPageBinding>(),
    ToolbarIconInteraction {
    override val layoutResId: Int = R.layout.fragment_protector_my_page

    private lateinit var toolbarHelper: ToolbarHelper

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()

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
        profileViewModel.getUserProfile(accessToken = getAccessTokenOrNull() ?: "")
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
                getInAppUpdateCheckerOrNull()?.checkNewestVersion(
                    onSuccess = { newestVersion, version ->
                        versionCheck.text =
                            getString(if (newestVersion == version) R.string.my_version_newest else R.string.my_version_need_update)
                    },
                    onFailure = {
                        versionCheck.text = ""
                    })
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        // 사용자 프로필
        profileViewModel.userProfile.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val data = state.data

                    with(binding) {
                        profileData = data
                        profileLinkUserCount.text = getString(R.string.my_account_linked_kids_count, data.linkedUser)
                        level = LevelModel(
                            previousLevel = if ((data.memberPoint.level - 1) < 0) "0" else (data.memberPoint.level - 1).toString(),
                            currentLevel = data.memberPoint.level.toString(),
                            nextLevel = (data.memberPoint.level + 1).toString()
                        )
                        Glide.with(requireContext()).load(data.profileUrl)
                            .error(R.drawable.ic_launcher_background)
                            .into(this.profileImage)
                        pointTitle.text = SpannableBuilder.build(pointNowHas.context) {
                            span(text = "다음 계단까지\n", textColor = R.color.gray_800, style = R.style.subtitle_16_600)
                            span(text = (100 - (data.memberPoint.point % 100)).toString() + "P ", textColor = R.color.blue_500, style = R.style.subtitle_16_600)
                            span(text = "남았어요!", textColor = R.color.gray_800, style = R.style.subtitle_16_600)
                        }
                        pointNowHas.text = SpannableBuilder.build(pointNowHas.context) {
                            span(text = "보유한 포인트 ", textColor = R.color.gray_600, style = R.style.body_13_500)
                            span(text = data.memberPoint.point.toString() + "P", textColor = R.color.blue_500, style = R.style.body_13_500)
                        }
                    }
                }
                is ModelState.Error -> {

                }
                is ModelState.Loading -> {}
            }
        }
    }

    fun onClickLinkUser() {
        val bottomSheet = CommonBottomSheetHelper.getInstance(
            data = CommonBottomSheetModel(
                type = BottomSheetType.PROFILE_IMAGE,
                title = "나와 연동된 아이".toSpannable(),
                subTitle = SpannableBuilder.build(binding.profileLinkUserCount.context) {
                    span(text = "메인홈 > 연동관리", textColor = R.color.blue_500, style = R.style.body_13_500)
                    span(text = "에서 정보 수정이 가능해요", textColor = R.color.gray_500, style = R.style.body_13_500)
                },
                contentList = linkedUserViewModel.getLinkedUserList() ?: listOf(""),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.ONE,
                    positiveButtonText = "닫기"
                )
            )
        )

        bottomSheet.show(childFragmentManager, null)
    }

    fun onClickRanking() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_protectorRankingFragment)
    }

    fun onClickPointHistory() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_pointHistoryFragment2)
    }

    fun onClickPointRule() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_pointRuleFragment2)
    }

    fun onClickCustomerService() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_protectorCSFragment)
    }

    fun onClickNotice() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_myNoticeFragment)

    }

    fun onClickManageAccount() {
        val bundle = Bundle().apply {
            putString(MyAccountManagementFragment.ARGUMENT_NICKNAME_KEY, profileViewModel.getUserNickname())
        }
        findNavController().navigate(
            R.id.action_protectorMyPageFragment_to_myAccountManagementFragment,
            bundle
        )
    }

    fun onClickUsingTerms() {
        val bundle = Bundle().apply {
            putParcelable(TermDetailFragment.ARGUMENT_TYPE_KEY, TermType.SERVICE)
        }
        findNavController().navigate(
            R.id.action_protectorMyPageFragment_to_termDetailFragment,
            bundle
        )
    }

    fun onClickPrivacyPolicy() {
        val bundle = Bundle().apply {
            putParcelable(TermDetailFragment.ARGUMENT_TYPE_KEY, TermType.PRIVACY)
        }
        findNavController().navigate(
            R.id.action_protectorMyPageFragment_to_termDetailFragment,
            bundle
        )
    }

    override fun onToolbarIconClicked() {
        findNavController().navigate(R.id.action_protectorMyPageFragment_to_protectorProfileModifyFragment)
    }
}