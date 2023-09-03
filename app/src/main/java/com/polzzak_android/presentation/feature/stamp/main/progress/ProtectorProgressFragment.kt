package com.polzzak_android.presentation.feature.stamp.main.progress

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
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
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel


class ProtectorProgressFragment : BaseFragment<FragmentProgressBinding>(), MainProgressInteraction {

    override val layoutResId: Int = R.layout.fragment_progress

    private var rvAdapter: MainStampAdapter = MainStampAdapter(emptyList(), this)
    private lateinit var vpAdapter: MainStampPagerAdapter

    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()
    private val stampViewModel: StampViewModel by activityViewModels()

    companion object {
        private var instance: ProtectorProgressFragment? = null

        @JvmStatic
        fun getInstance(): ProtectorProgressFragment {
            if (instance == null) {
                synchronized(ProtectorProgressFragment::class.java) {
                    if (instance == null) {
                        instance = ProtectorProgressFragment()
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

        linkedUserViewModel.requestLinkedUserList(accessToken = getAccessTokenOrNull() ?: "")
        val hasLinkedUser = linkedUserViewModel.hasLinkedUser
        if (hasLinkedUser) {
            // 도장판 조회
            stampViewModel.requestStampBoardList(
                accessToken = getAccessTokenOrNull() ?: "",
                linkedMemberId = null,
                stampBoardGroup = "in_progress"     // 진행 중 todo: enum class
            )

            // 당겨서 리프레시
            binding.stampListRefresh.setOnRefreshListener {
                stampViewModel.requestStampBoardList(
                    accessToken = getAccessTokenOrNull() ?: "",
                    linkedMemberId = null,
                    stampBoardGroup = "in_progress"     // 진행 중 todo: enum class
                )
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        // 연동된 사용자 있는지 확인
        linkedUserViewModel.linkedUserList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
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

                is ModelState.Error -> {
                    // todo: 에러 페이지
                }

                is ModelState.Loading -> {
                    // todo: 스켈레톤
                }
            }
        }

        // 도장판 목록 조회
        stampViewModel.stampBoardList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val data = state.data
                    // todo: 바인더블 어댑터로 변경
                    rvAdapter = MainStampAdapter(data, this)
                    binding.stampListRc.adapter = rvAdapter

                    binding.skeletonLoadingView.visibility = View.GONE
                }

                is ModelState.Error -> {
                    // todo: 에러
                    binding.skeletonLoadingView.visibility = View.GONE
                }

                is ModelState.Loading -> {
                    // todo: 로딩 스켈레톤
                    binding.skeletonLoadingView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setAdapter() {
        binding.stampListRc.adapter = rvAdapter
        binding.stampListRc.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun clickUserFilter() {
        val userList = linkedUserViewModel.getLinkedUserList()

        if (userList != null) {
            CommonBottomSheetHelper.getInstance(
                data = CommonBottomSheetModel(
                    type = BottomSheetType.SELECT_STAMP_BOARD,
                    title = "누구의 도장판을 볼까요?",
                    contentList = userList.map { it.toSelectUserStampBoardModel() },
                    button = CommonButtonModel(
                        buttonCount = ButtonCount.ZERO
                    )
                ),
                onClickListener = {
                    object : OnButtonClickListener {
                        override fun setBusinessLogic() {}

                        override fun getReturnValue(value: Any) {
                            val selectedUserInfo = (value as SelectUserStampBoardModel)
                            binding.selectContainer.visibility = View.GONE

                            stampViewModel.requestStampBoardList(
                                accessToken = getAccessTokenOrNull() ?: "",
                                linkedMemberId = selectedUserInfo.userId.toString(),
                                stampBoardGroup = "in_progress"     // 진행 중 todo: enum class
                            )
                        }

                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    override fun setViewPager(
        view: ViewPager2,
        curInd: TextView,
        totalInd: TextView,
        stampList: List<StampBoardSummaryModel>?
    ) {
        // todo: adapter 임시
        vpAdapter = MainStampPagerAdapter(stampList, this)
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

    override fun onStampPagerClicked(stampBoardItem: StampBoardSummaryModel) {
        findNavController().navigate(
            R.id.action_to_stampBoardDetailFragment,
            Bundle().apply { putInt("boardId", stampBoardItem.stampBoardId) }
        )
    }

    override fun setProgressAnim(curCnt: Int, totalCnt: Int, view: SemiCircleProgressView) {
        val degree = stampProgressCalculator(curCnt, totalCnt)
        view.setAnimation(degree)
    }
}