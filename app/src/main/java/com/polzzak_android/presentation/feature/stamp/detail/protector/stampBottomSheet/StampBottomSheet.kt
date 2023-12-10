package com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.polzzak_android.R
import com.polzzak_android.databinding.CommonBottomSheetStampBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getAccessTokenOrNull
import com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.bindable.BottomSheetStampListClickInteraction
import com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.bindable.StampBottomSheetMissionListClickInteraction
import com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.bindable.StampBottomSheetMissionListItem
import com.polzzak_android.presentation.feature.stamp.detail.protector.stampBottomSheet.bindable.StampBottomSheetStampListItem
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

/**
 * [MakeStampBottomSheet]로 대체되었습니다.
 */
@Deprecated("더 이상 사용하지 않음")
class StampBottomSheet : BottomSheetDialogFragment(), StampBottomSheetMissionListClickInteraction,
    BottomSheetStampListClickInteraction {
    private var _binding: CommonBottomSheetStampBinding? = null
    private val binding get() = _binding!!

    private val missionItems = mutableListOf<BindableItem<*>>()
    private var missionAdapter: BindableItemAdapter = BindableItemAdapter()
    private val stampItems = mutableListOf<BindableItem<*>>()
    private var stampAdapter: BindableItemAdapter = BindableItemAdapter()

    private lateinit var data: List<MissionModel>
    private lateinit var viewModel: StampBottomSheetViewModel
    private var stampBoardId: Int? = null

    companion object {
        fun getInstance(
            data: List<MissionModel>,
            viewModel: StampBottomSheetViewModel,
            stampBoardId: Int
        ): StampBottomSheet {
            val instance = StampBottomSheet()
            with(instance) {
                this.data = data
                this.viewModel = viewModel
                this.stampBoardId = stampBoardId
            }
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommonBottomSheetStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        binding.bottomSheet = this
        binding.bottomSheetPositiveButton.isEnabled = false

        setUpAdapter()
        setUpMissionListItem()
        setUpStampListItem()
    }

    private fun initObserve() {
        // 현 스텝에 따라 하단 버튼 기능 및 텍스트 제어
        viewModel.isFirstStep.observe(viewLifecycleOwner) { isFirstStep ->
            binding.isFirstStep = isFirstStep

            when (isFirstStep) {
                true -> {
                    with(binding) {
                        bottomSheetTitle.text = "도장 요청 선택"
                        bottomSheetSubtitle.text = "1/2"
                    }

                    with(binding.bottomSheetPositiveButton) {
                        text = "다음"
                        setOnClickListener {
                            viewModel.updateStep(isFirstStep = false)
                        }
                    }

                    with(binding.bottomSheetNegativeButton) {
                        text = "닫기"
                        setOnClickListener {
                            dismiss()
                        }
                    }
                }

                false -> {
                    with(binding) {
                        bottomSheetTitle.text = "도장 선택"
                        bottomSheetSubtitle.text = "2/2"
                    }

                    with(binding.bottomSheetPositiveButton) {
                        text = "도장 찍기"
                        setOnClickListener {
                            viewModel.makeStamp(
                                getAccessTokenOrNull() ?: "",
                                stampBoardId = stampBoardId ?: -1
                            )
                        }
                    }

                    with(binding.bottomSheetNegativeButton) {
                        text = "이전"
                        setOnClickListener {
                            viewModel.updateStep(isFirstStep = true)
                        }
                    }
                }
            }
        }

        // 선택한 도장
        viewModel.selectedStamp.observe(viewLifecycleOwner) { stamp ->
            when (stamp.id) {
                1 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_1)
                2 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_2)
                3 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_3)
                4 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_4)
                5 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_5)
                6 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_6)
                7 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_7)
                8 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_8)
                9 -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_9)
                else -> binding.selectedStamp.setImageResource(R.drawable.ic_stamp_1)
            }
        }
    }

    private fun setUpAdapter() {
        with(binding) {
            bottomSheetRecyclerView.adapter = missionAdapter
            selectingStampRv.adapter = stampAdapter
        }
    }

    private fun setUpMissionListItem() {
        missionItems.clear()
        missionItems.addAll(data.map { model ->
            StampBottomSheetMissionListItem(
                model = model,
                interaction = this
            )
        })
        this.missionAdapter.updateItem(item = missionItems)
    }

    private fun setUpStampListItem() {
        stampItems.clear()
        val stampList = getCompleteStampList()
        stampItems.addAll(stampList.map {
            StampBottomSheetStampListItem(
                model = it,
                interaction = this
            )
        })
        this.stampAdapter.updateItem(item = stampItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectItem(items: List<BindableItem<*>>, modelId: Int): BindableItem<*>? {
        var selectedItem: BindableItem<*>? = null
        items.forEach { item ->
            item as StampBottomSheetMissionListItem
            item.isSelected = item.model.id == modelId
            if (item.isSelected) selectedItem = item
        }
        return selectedItem
    }

    override fun onMissionClick(model: MissionModel) {
        selectItem(missionItems, model.id)?.let {
            missionAdapter.notifyDataSetChanged()
            viewModel.setSelectedMission(model)
            binding.bottomSheetPositiveButton.isEnabled = true
        }
    }

    override fun onStampClick(stampId: CompleteStampModel) {
        viewModel.setSelectedStamp(stampId)
    }
}