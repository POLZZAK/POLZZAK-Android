package com.polzzak_android.presentation.main.protector.progress

import android.graphics.Rect
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.util.stampProgressCalculator
import com.polzzak_android.databinding.FragmentProgressBinding
import com.polzzak_android.presentation.adapter.MainStampAdapter
import com.polzzak_android.presentation.adapter.MainStampPagerAdapter
import com.polzzak_android.presentation.main.protector.StampViewModel
import com.polzzak_android.presentation.component.SelectUserFilterFragment
import com.polzzak_android.presentation.component.SemiCircleProgressView
import com.polzzak_android.presentation.main.intercation.MainProgressInteraction
import com.polzzak_android.presentation.main.model.Partner
import com.polzzak_android.presentation.main.model.StampBoard
import com.polzzak_android.presentation.main.model.StampBoardSummary

class ProtectorProgressFragment : BaseFragment<FragmentProgressBinding>(), MainProgressInteraction {

    override val layoutResId: Int = R.layout.fragment_progress

    private lateinit var rvAdapter: MainStampAdapter
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
            val sheet = SelectUserFilterFragment.newInstance()
            sheet.show(childFragmentManager, "selectUserSheet")
        }

        binding.stampListRefresh.setOnRefreshListener {
            rvAdapter.notifyDataSetChanged()
            binding.stampListRefresh.isRefreshing = false
        }
    }

    private fun setAdapter() {
        // Todo: dummy data 변경
        val dummy = listOf<StampBoard>(
            StampBoard(
                type = 2,
                Partner(
                    kid = false,
                    memberId = 2,
                    memberType = "ECT",
                    nickname = " 연동o도장판o",
                    profileUrl = ""
                ),
                listOf<StampBoardSummary>(
                    StampBoardSummary(
                        currentStampCount = 10,
                        goalStampCount = 20,
                        isRewarded = false,
                        missionCompleteCount = 3,
                        name = "도장판 이름1",
                        reward = "보상1",
                        stampBoardId = 1
                    ),
                    StampBoardSummary(
                        currentStampCount = 10,
                        goalStampCount = 20,
                        isRewarded = false,
                        missionCompleteCount = 3,
                        name = "도장판 이름2",
                        reward = "보상2",
                        stampBoardId = 2
                    ),
                ),
            ),
            StampBoard(
                type = 1,
                Partner(
                    kid = false,
                    memberId = 1,
                    memberType = "ECT",
                    nickname = "연동o도장판x11",
                    profileUrl = ""
                ),
                listOf<StampBoardSummary>(

                ),
            ),
            StampBoard(
                type = 1,
                Partner(
                    kid = false,
                    memberId = 3,
                    memberType = "ECT",
                    nickname = "연동o도장판x22",
                    profileUrl = ""
                ),
                listOf<StampBoardSummary>(

                ),
            )
        )

        rvAdapter = MainStampAdapter(dummy, this)
        binding.stampListRc.adapter = rvAdapter
        binding.stampListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun setViewPager(view: ViewPager2, curInd: TextView, totalInd: TextView, stampList: List<StampBoardSummary>) {
        // adapter
        vpAdapter = MainStampPagerAdapter(stampList, this)
        view.adapter = vpAdapter

        // indicator
        // Todo: 임시
        curInd.text = "1"
        totalInd.text = resources.getString(R.string.viewpager_indicator, stampList.size)
        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curInd.text = position.plus(1).toString()
                curInd.invalidate()
            }
        })
    }

    override fun onStampPagerClicked(stampBoardItem: StampBoardSummary) {
        // Todo: 임시
        Toast.makeText(context, "${stampBoardItem.name} 클릭", Toast.LENGTH_SHORT).show()
    }

    override fun setProgressAnim(curCnt: Int, totalCnt: Int, view: SemiCircleProgressView) {
        val degree = stampProgressCalculator(curCnt, totalCnt)
        view.setAnimation(degree)
    }
}