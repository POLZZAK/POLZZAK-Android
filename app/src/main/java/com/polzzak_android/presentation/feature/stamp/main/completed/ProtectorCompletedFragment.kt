package com.polzzak_android.presentation.feature.stamp.main.completed

import android.view.View
import android.widget.TextView
import androidx.core.text.toSpannable
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentCompletedBinding
import com.polzzak_android.presentation.common.adapter.MainCouponAdapter
import com.polzzak_android.presentation.common.adapter.MainStampCompletedAdapter
import com.polzzak_android.presentation.common.adapter.MainStampCompletedPagerAdapter
import com.polzzak_android.presentation.common.adapter.MainStampPagerAdapter
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.component.bottomsheet.BottomSheetType
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetHelper
import com.polzzak_android.presentation.component.bottomsheet.CommonBottomSheetModel
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserStampBoardModel
import com.polzzak_android.presentation.component.bottomsheet.model.toSelectUserStampBoardModel
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.feature.stamp.intercation.MainCompletedInteraction
import com.polzzak_android.presentation.feature.stamp.main.protector.StampLinkedUserViewModel
import com.polzzak_android.presentation.feature.stamp.main.protector.StampViewModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel

class ProtectorCompletedFragment : BaseFragment<FragmentCompletedBinding>(),
    MainCompletedInteraction {
    override val layoutResId: Int = R.layout.fragment_completed

    private var rvAdapter: MainStampCompletedAdapter = MainStampCompletedAdapter(emptyList(), this)
    private lateinit var vpAdapter: MainStampCompletedPagerAdapter

    private val linkedUserViewModel: StampLinkedUserViewModel by activityViewModels()
    private val stampViewModel: StampViewModel by activityViewModels()

    companion object {
        private var instance: ProtectorCompletedFragment? = null

        @JvmStatic
        fun getInstance(): ProtectorCompletedFragment {
            if (instance == null) {
                synchronized(ProtectorCompletedFragment::class.java) {
                    if (instance == null) {
                        instance = ProtectorCompletedFragment()
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
                stampBoardGroup = "ended"     // 진행 중 todo: enum class
            )
        }
    }

    fun clickUserFilter() {
        val userList = linkedUserViewModel.getLinkedUserList()

        if (userList != null) {
            CommonBottomSheetHelper.getInstance(
                data = CommonBottomSheetModel(
                    type = BottomSheetType.SELECT_STAMP_BOARD,
                    title = "누구의 도장판을 볼까요?".toSpannable(),
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
                                stampBoardGroup = "ended" // 진행 중 todo: enum class
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
                        stampBoardGroup = "ended"     // 진행 중 todo: enum class
                    )
                }
            }
        }

        // 도장판 목록 조회
        stampViewModel.stampBoardList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val data = state.data
                    rvAdapter.setCompletedStampList(data)
                    binding.stampListRc.adapter = rvAdapter

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
        stampList: List<StampBoardSummaryModel>?
    ) {
        vpAdapter = MainStampCompletedPagerAdapter(stampList)
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
}