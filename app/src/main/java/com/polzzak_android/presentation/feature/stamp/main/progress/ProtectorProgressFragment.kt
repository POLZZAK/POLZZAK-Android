package com.polzzak_android.presentation.feature.stamp.main.progress

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.common.util.Utility
import com.polzzak_android.R
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.common.util.stampProgressCalculator
import com.polzzak_android.databinding.FragmentProgressBinding
import com.polzzak_android.presentation.common.adapter.MainStampAdapter
import com.polzzak_android.presentation.common.adapter.MainStampPagerAdapter
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.feature.stamp.main.protector.StampViewModel
import com.polzzak_android.presentation.component.SemiCircleProgressView
import com.polzzak_android.presentation.feature.stamp.intercation.MainProgressInteraction
import com.polzzak_android.presentation.feature.stamp.model.PartnerModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardModel
import com.polzzak_android.presentation.feature.stamp.model.StampBoardSummaryModel
import timber.log.Timber

class ProtectorProgressFragment : BaseFragment<FragmentProgressBinding>(), MainProgressInteraction {

    override val layoutResId: Int = R.layout.fragment_progress

    private var rvAdapter: MainStampAdapter = MainStampAdapter(emptyList(), this)
    private lateinit var vpAdapter: MainStampPagerAdapter
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
        binding.stampViewModel = stampViewModel

        setAdapter()

        // Todo: 선택 바텀 시트 의논 사항-> 네비게이션으로 관리할지, 인스턴스 새롭게 생성할지?
        binding.selectTxt.text = "전체"
        binding.selectContainer.setOnClickListener {
        // todo: 바텀시트 연결
        }

        binding.stampListRefresh.setOnRefreshListener {
            rvAdapter.notifyDataSetChanged()
            binding.stampListRefresh.isRefreshing = false
        }

        stampViewModel.getStampBoardList(
            accessToken = getAccessTokenOrNull() ?: "",
            linkedMemberId = null,
            stampBoardGroup = "in_progress"     // 진행 중 todo: enum class
        )
    }

    override fun initObserver() {
        super.initObserver()
        stampViewModel.stampBoardList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val data = state.data
                    rvAdapter = MainStampAdapter(listOf(data), this)
                    rvAdapter.setStampList(listOf(data))
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

    private fun setAdapter() {
        binding.stampListRc.adapter = rvAdapter
        binding.stampListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<StampBoardSummaryModel>?) {
        // adapter
        vpAdapter = MainStampPagerAdapter(stampList, this)
        view.adapter = vpAdapter

        // indicator
        // Todo: 임시
        curInd.text = "1"
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
        // Todo: 임시
        Toast.makeText(context, "${stampBoardItem.name} 클릭", Toast.LENGTH_SHORT).show()

        // TODO: 부모 nav_graph.xml에도 같은 action 추가 필요
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