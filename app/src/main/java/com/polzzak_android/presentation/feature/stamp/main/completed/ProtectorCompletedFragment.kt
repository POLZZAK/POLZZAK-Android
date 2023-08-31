package com.polzzak_android.presentation.feature.stamp.main.completed

import androidx.fragment.app.activityViewModels
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.databinding.FragmentCompletedBinding
import com.polzzak_android.presentation.common.adapter.MainStampAdapter
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.feature.stamp.main.protector.StampViewModel

class ProtectorCompletedFragment : BaseFragment<FragmentCompletedBinding>() {
    override val layoutResId: Int = R.layout.fragment_completed

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

        stampViewModel.checkHasLinkedUser(accessToken = getAccessTokenOrNull() ?: "")
        val hasLinkedUser = stampViewModel.hasLinkedUser.value?.data
        if (hasLinkedUser == true) {
            // 도장판 조회
            stampViewModel.getStampBoardList(
                accessToken = getAccessTokenOrNull() ?: "",
                linkedMemberId = null,
                stampBoardGroup = "complete"     // 진행 중 todo: enum class
            )

//            // 바텀시트
//            binding.selectTxt.text = "전체"
//            binding.selectContainer.setOnClickListener {
//                // todo: 바텀시트 연결
//            }
//
//            // 당겨서 리프레시
//            binding.stampListRefresh.setOnRefreshListener {
//                rvAdapter.notifyDataSetChanged()
//                binding.stampListRefresh.isRefreshing = false
//            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        // 연동된 사용자 있는지 확인
        stampViewModel.hasLinkedUser.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val hasLinkedUser = state.data
                    binding.hasLinkedUser = hasLinkedUser
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
//                    rvAdapter = MainStampAdapter(listOf(data), this)
//                    rvAdapter.setStampList(listOf(data))
                }

                is ModelState.Error -> {
                    // todo: 에러
                }

                is ModelState.Loading -> {
                    // todo: 로딩 스켈레톤
                }
            }
        }
    }
}