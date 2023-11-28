package com.polzzak_android.presentation.feature.stamp.main.progress

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.text.toSpannable
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentProgressBinding
import com.polzzak_android.presentation.common.adapter.MainStampAdapter
import com.polzzak_android.presentation.common.adapter.MainStampPagerAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.common.util.stampProgressCalculator
import com.polzzak_android.presentation.component.SemiCircleProgressView
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserStampBoardModel
import com.polzzak_android.presentation.component.bottomsheet.model.toSelectUserStampBoardModel
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.feature.stamp.intercation.MainProgressInteraction
import com.polzzak_android.presentation.feature.stamp.main.protector.StampLinkedUserViewModel
import com.polzzak_android.presentation.feature.stamp.main.protector.StampViewModel
import com.polzzak_android.presentation.feature.stamp.model.PartnerModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel


class ProtectorProgressFragment : BaseFragment<FragmentProgressBinding>(), MainProgressInteraction {

    override val layoutResId: Int = R.layout.fragment_progress

    private var rvAdapter: MainStampAdapter = MainStampAdapter(emptyList(), this)
    private lateinit var vpAdapter: MainStampPagerAdapter

    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()
    private val stampViewModel: StampViewModel by activityViewModels()

    val isKid: Boolean by lazy {
        arguments?.getBoolean(ARG_IS_KID) ?: false
    }

    companion object {
        private const val ARG_IS_KID = "isKid"
        private var instance: ProtectorProgressFragment? = null

        @JvmStatic
        fun getInstance(isKid: Boolean): ProtectorProgressFragment {
            if (instance == null) {
                synchronized(ProtectorProgressFragment::class.java) {
                    if (instance == null) {
                        instance = ProtectorProgressFragment().apply {
                            arguments = Bundle().apply { putBoolean(ARG_IS_KID, isKid) }
                        }
                    }
                }
            }
            return instance!!
        }
    }

    override fun initView() {
        super.initView()

        binding.lifecycleOwner = this
        binding.fragment = this

        setAdapter()
        setRefreshListener()

        linkedUserViewModel.requestLinkedUserList(accessToken = getAccessTokenOrNull() ?: "")
    }

    private fun setAdapter() {
        binding.stampListRc.adapter = rvAdapter
        binding.stampListRc.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setRefreshListener() {
        binding.stampListRefresh.setOnRefreshListener {
            stampViewModel.requestStampBoardList(
                accessToken = getAccessTokenOrNull() ?: "",
                linkedMemberId = stampViewModel.getSelectedUserId(),
                stampBoardGroup = "in_progress"     // 진행 중 todo: enum class
            )
        }
    }

    fun clickUserFilter() {
        val userList = linkedUserViewModel.getLinkedUserList()

        if (userList != null) {
            val title = if (isKid) {
                "누가 만들어준 도장판을 볼까요?"
            } else {
                "누구의 도장판을 볼까요?"
            }

            CommonBottomSheetHelper.getInstance(
                data = CommonBottomSheetModel(
                    type = BottomSheetType.SELECT_STAMP_BOARD,
                    title = title.toSpannable(),
                    contentList = listOf(SelectUserStampBoardModel(userId = 0, nickName = "전체", userType = null)) + userList.map { it.toSelectUserStampBoardModel() },
                    button = CommonButtonModel(
                        buttonCount = ButtonCount.ZERO
                    )
                ),
                onClickListener = {
                    object : OnButtonClickListener {
                        override fun setBusinessLogic() {}

                        override fun getReturnValue(value: Any) {
                            val selectedUserInfo = value as SelectUserStampBoardModel
                            val selectedUserId = if (selectedUserInfo.nickName == "전체") null else selectedUserInfo.userId

                            stampViewModel.setSelectedUserId(userId = selectedUserId)
                            stampViewModel.requestStampBoardList(
                                accessToken = getAccessTokenOrNull() ?: "",
                                linkedMemberId = selectedUserId?.toString(),
                                stampBoardGroup = "in_progress" // 진행 중 todo: enum class
                            )
                        }

                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    override fun initObserver() {
        super.initObserver()
        // 연동된 사용자 있는지 확인
        linkedUserViewModel.linkedUserList.observe(viewLifecycleOwner) { state ->
            if (state is ModelState.Success) {
                val hasLinkedUser = linkedUserViewModel.hasLinkedUser
                binding.hasLinkedUser = hasLinkedUser

                if (hasLinkedUser) {
                    // 도장판 조회
                    stampViewModel.requestStampBoardList(
                        accessToken = getAccessTokenOrNull() ?: "",
                        linkedMemberId = null,
                        stampBoardGroup = "in_progress"     // 진행 중 todo: enum class
                    )
                }
            }
        }

        // 도장판 목록 조회
        stampViewModel.stampBoardList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val data = state.data
                    rvAdapter.setStampList(data)
                    binding.stampListRc.adapter = rvAdapter

                    // FIXME: 목록 총 개수가 1개면 무조건 특정 사용자가 필터링된 것이라고 간주하고 해당 닉네임 표시해버림
                    //        연결된 사용자가 한명이면 "전체"를 표시할 수 없음
                    val selectedUser = if (data.size == 1) data.first().partner?.nickname else "전체"
                    binding.selectTxt.text = selectedUser

                    with(binding) {
                        mainContainer.visibility = View.VISIBLE
                        skeleton.visibility = View.GONE
                        stampListRefresh.isRefreshing = false
                    }
                }

                is ModelState.Error -> {
                    with(binding) {
                        mainContainer.visibility = View.VISIBLE
                        skeleton.visibility = View.GONE
                    }
                }

                is ModelState.Loading -> {
                    with(binding) {
                        mainContainer.visibility = View.GONE
                        skeleton.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun setViewPager(
        view: ViewPager2,
        curInd: TextView,
        totalInd: TextView,
        stampList: List<StampBoardSummaryModel>?,
        partner: PartnerModel
    ) {
        vpAdapter = MainStampPagerAdapter(stampList, this, partner)
        view.adapter = vpAdapter

        // indicator
        curInd.text = "1"   // todo: 임시
        totalInd.text = resources.getString(R.string.viewpager_indicator, stampList?.size)
        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curInd.text = position.plus(1).toString()
                curInd.invalidate()
            }
        })
    }

    override fun onStampPagerClicked(stampBoardItem: StampBoardSummaryModel, partner: PartnerModel) {
        findNavController().navigate(
            resId = R.id.action_to_stampBoardDetailFragment,
            args = Bundle().apply {
                putInt("boardId", stampBoardItem.stampBoardId)
                putInt("partnerId", partner.memberId)
                putString("partnerType", partner.memberType)
            }
        )
    }

    override fun setProgressAnim(curCnt: Int, totalCnt: Int, view: SemiCircleProgressView) {
        val degree = stampProgressCalculator(curCnt, totalCnt)
        view.setAnimation(degree)
    }
}