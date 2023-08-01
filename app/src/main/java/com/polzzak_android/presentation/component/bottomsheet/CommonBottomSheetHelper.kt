package com.polzzak_android.presentation.component.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.polzzak_android.databinding.CommonBottomSheetBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.component.bottomsheet.bindable.BottomSheetMissionListClickInteraction
import com.polzzak_android.presentation.component.bottomsheet.bindable.MissionListItem
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

class CommonBottomSheetHelper(
    private val data: CommonBottomSheetModel,
    private val onPositiveButtonListener: (() -> OnButtonClickListener)? = null,
) : BottomSheetDialogFragment(), BottomSheetMissionListClickInteraction {

    private var _binding: CommonBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val items = mutableListOf<BindableItem<*>>()
    private var adapter: BindableItemAdapter = BindableItemAdapter()

    private var returnValue: Any? = null

    companion object {
        fun getInstance(
            data: CommonBottomSheetModel,
            onPositiveButtonListener: (() -> OnButtonClickListener)? = null
        ): CommonBottomSheetHelper {
            return CommonBottomSheetHelper(data, onPositiveButtonListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommonBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.data = data
        binding.bottomSheet = this

        binding.bottomSheetPositiveButton.isEnabled = false

        setUpAdapter()
        setUpListItem()
    }

    private fun setUpAdapter() {
        binding.bottomSheetRecyclerView.adapter = this.adapter
    }

    private fun setUpListItem() {
        items.clear()

        when (data.type) {
            BottomSheetType.MISSION -> {
                items.addAll(data.contentList.map { model ->
                    MissionListItem(
                        model = model as MissionModel,
                        interaction = this
                    )
                })
            }

            BottomSheetType.PROFILE_IMAGE -> {

            }

            BottomSheetType.PROFILE_LIST -> {

            }
        }

        this.adapter.updateItem(item = items)
    }

    fun onNegativeButtonClick() {
        dismiss()
    }

    fun onPositiveButtonClick() {
        onPositiveButtonListener?.let {
            with(it.invoke()) {
                setBusinessLogic()
                returnValue?.let { value -> getReturnValue(value) }
            }
        }

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMissionClick(model: MissionModel) {
        items.forEach { item ->
            if (item is MissionListItem) {
                item.isSelected = item.model.id == model.id
            }
        }

        adapter.notifyDataSetChanged()
        this.returnValue = model

        binding.bottomSheetPositiveButton.isEnabled = true
    }

}