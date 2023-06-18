package com.polzzak_android.presentation.makingStamp

import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentMakeStampBinding
import com.polzzak_android.presentation.adapter.MakeStampCountAdapter
import com.polzzak_android.presentation.adapter.MakeStampMissionAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeStampFragment : BaseFragment<FragmentMakeStampBinding>(), StampCountInteraction,
    MissionInteraction {
    override val layoutResId: Int = R.layout.fragment_make_stamp

    private lateinit var makeStampCountAdapter: MakeStampCountAdapter
    private lateinit var stampCountSelectHelper: StampCountSelectedHelper
    private lateinit var stampMissionAdapter: MakeStampMissionAdapter

    private val makeStampViewModel: MakeStampViewModel by activityViewModels()

    override fun initView() {
        super.initView()
        stampCountSelectHelper = StampCountSelectedHelper.getInstance()

        setAdapter()
        initData()

        binding.missionEnrollButton.setOnClickListener {
            makeStampViewModel.createMission()
        }

        binding.missionExButton.setOnClickListener {
            Toast.makeText(context, "미션 예시", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter() {
        // 도장판 개수
        makeStampCountAdapter =
            MakeStampCountAdapter(binding.stampCountRc, this, stampCountSelectHelper)
        binding.stampCountRc.adapter = makeStampCountAdapter

        // 미션
        stampMissionAdapter = MakeStampMissionAdapter(this)
        binding.missionRc.adapter = stampMissionAdapter
    }

    override fun initObserver() {
        super.initObserver()
        makeStampViewModel.missionList.observe(this) { missionList ->
            stampMissionAdapter.submitList(missionList)
        }
        makeStampViewModel.missionListSize.observe(this) { missionListSize ->
            stampMissionAdapter.setMissionListSize(missionListSize)

            if (missionListSize < 50) {
                binding.missionEnrollButton.apply {
                    isEnabled = true
                    text = "+ 미션 추가"
                }
            } else {
                binding.missionEnrollButton.apply {
                    isEnabled = false
                    text = "미션은 50개까지 만들 수 있어요"
                }
            }
        }
    }

    private fun initData() {
        // 도장판 개수
        makeStampCountAdapter.submitList(listOf(10, 12, 16, 20, 25, 30, 36, 40, 48, 60))        // todo: 하드코딩 바꾸기?
        stampCountSelectHelper.stampCount = makeStampViewModel.getStampCountList()!!
        makeStampViewModel.setMissionListSize(makeStampViewModel.getMissionListSize())
    }

    override fun onStampCountClicked(view: TextView, value: Int) {
        stampCountSelectHelper.onCountClicked(view = view, value = value)
    }

    override fun onDeletedIconClicked(mission: String, view: ImageButton) {
        makeStampViewModel.deleteMission(mission)
    }

    override fun updateMissionList(missionList: List<String>) {
        makeStampViewModel.updateMissionList(missionList)
    }

}