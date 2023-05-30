package com.polzzak_android.presentation.main.protector.progress

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.polzzak_android.R
import com.polzzak_android.common.base.BaseFragment
import com.polzzak_android.common.util.stampProgressCalculator
import com.polzzak_android.data.remote.model.StampBoardGroup
import com.polzzak_android.databinding.FragmentProgressBinding
import com.polzzak_android.presentation.adapter.MainStampAdapter
import com.polzzak_android.presentation.adapter.MainStampPagerAdapter
import com.polzzak_android.presentation.main.protector.ProtectorStampViewModel
import com.polzzak_android.presentation.main.model.StampBoardSummary
import com.polzzak_android.presentation.component.SelectUserFilterFragment
import com.polzzak_android.presentation.component.SemiCircleProgressView
import com.polzzak_android.presentation.main.intercation.MainProgressInteraction
import kotlinx.coroutines.launch

class ProtectorProgressFragment : BaseFragment<FragmentProgressBinding>(), MainProgressInteraction {

    override val layoutResId: Int = R.layout.fragment_progress

    private lateinit var rvAdapter: MainStampAdapter
    private lateinit var vpAdapter: MainStampPagerAdapter
    private val stampViewModel: ProtectorStampViewModel by activityViewModels()

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

        binding.selectTxt.text = "전체"
        binding.selectContainer.setOnClickListener {
            val sheet = SelectUserFilterFragment.newInstance()
            sheet.show(childFragmentManager, "selectUserSheet")
        }

        lifecycleScope.launch {
            // todo: 하드코딩 변경
            stampViewModel.setStampList(group = StampBoardGroup.IN_PROGRESS)
        }

        binding.stampListRefresh.setOnRefreshListener {
            rvAdapter.notifyDataSetChanged()
            binding.stampListRefresh.isRefreshing = false
        }
    }

    override fun initObserver() {
        super.initObserver()
        stampViewModel.stampList.observe(this, Observer { stampList ->
            if (stampList != null) {
                rvAdapter.setStampList(stampList)
            }
        })
    }

    private fun setAdapter() {
        rvAdapter = MainStampAdapter(this)
        binding.stampListRc.adapter = rvAdapter
        binding.stampListRc.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun setViewPager(
        view: ViewPager2,
        curInd: TextView,
        totalInd: TextView,
        stampList: List<StampBoardSummary>
    ) {
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