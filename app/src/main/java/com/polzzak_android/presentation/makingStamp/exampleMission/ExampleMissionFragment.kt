package com.polzzak_android.presentation.makingStamp.exampleMission

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.polzzak_android.R
import com.polzzak_android.databinding.FragmentExampleMissionBinding
import com.polzzak_android.databinding.ItemExampleMissionBinding
import com.polzzak_android.presentation.adapter.MakeExampleMissionAdapter
import com.polzzak_android.presentation.common.base.BaseFragment
import com.polzzak_android.presentation.makingStamp.MakeStampViewModel
import com.polzzak_android.presentation.makingStamp.ExampleMissionHelper
import com.polzzak_android.presentation.makingStamp.intreraction.ExampleMissionInteraction


class ExampleMissionFragment : BaseFragment<FragmentExampleMissionBinding>(),
    ExampleMissionInteraction {
    override val layoutResId: Int = R.layout.fragment_example_mission

    private lateinit var makeExampleMissionAdapter: MakeExampleMissionAdapter

    private lateinit var exampleMissionHelper: ExampleMissionHelper

    private var currentMissionListSize = 0

    private val makeStampViewModel: MakeStampViewModel by activityViewModels()

    override fun initView() {
        super.initView()
        exampleMissionHelper = ExampleMissionHelper.getInstance()

        setAdapter()
        initData()

        // todo: 임시 추가 툴바 단 후 삭제
        binding.tempButton.setOnClickListener {
            Toast.makeText(context, exampleMissionHelper.selectedExampleMission.toString(), Toast.LENGTH_SHORT).show()
            makeStampViewModel.addMissionList(exampleMissionHelper.selectedExampleMission)
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    private fun setAdapter() {
        makeExampleMissionAdapter = MakeExampleMissionAdapter(this, exampleMissionHelper)
        binding.missionExampleRc.adapter = makeExampleMissionAdapter
    }

    private fun initData() {
        makeExampleMissionAdapter.submitList(exampleMissionHelper.getList())
        currentMissionListSize = makeStampViewModel.getMissionListSize()
    }

    override fun onExampleMissionClicked(viewBinding: ItemExampleMissionBinding, value: String) {
        if (currentMissionListSize.plus(1) > 50) {
            Toast.makeText(context, "미션은 총 50개까지 등록할 수 있어요", Toast.LENGTH_SHORT).show()
            return
        }
        exampleMissionHelper.onMissionClicked(viewBinding = viewBinding, value = value)
        currentMissionListSize.plus(1)
    }
}