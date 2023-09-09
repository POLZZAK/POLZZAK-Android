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
import com.polzzak_android.presentation.component.bottomsheet.bindable.BottomSheetUserInfoImageListClickInteraction
import com.polzzak_android.presentation.component.bottomsheet.bindable.BottomSheetUserInfoListClickInteraction
import com.polzzak_android.presentation.component.bottomsheet.bindable.MissionListItem
import com.polzzak_android.presentation.component.bottomsheet.bindable.UserInfoImageListItem
import com.polzzak_android.presentation.component.bottomsheet.bindable.UserInfoListItem
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserMakeBoardModelModel
import com.polzzak_android.presentation.component.bottomsheet.model.SelectUserStampBoardModel
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

class CommonBottomSheetHelper(
    private val data: CommonBottomSheetModel,
    private val onClickListener: (() -> OnButtonClickListener)? = null,
) : BottomSheetDialogFragment(), BottomSheetMissionListClickInteraction,
    BottomSheetUserInfoListClickInteraction, BottomSheetUserInfoImageListClickInteraction {

    private var _binding: CommonBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val items = mutableListOf<BindableItem<*>>()
    private var adapter: BindableItemAdapter = BindableItemAdapter()

    private var returnValue: Any? = null

    companion object {
        fun getInstance(
            data: CommonBottomSheetModel,
            onClickListener: (() -> OnButtonClickListener)? = null
        ): CommonBottomSheetHelper {
            return CommonBottomSheetHelper(data, onClickListener)
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

            BottomSheetType.EXAMPLE_MISSION -> {
                var id = 1000000
                items.addAll(data.contentList.map { model ->
                    MissionListItem(
                        model = MissionModel(id = id++, content = model as String),
                        interaction = this
                    )
                })
            }

            BottomSheetType.PROFILE_IMAGE -> {
                items.addAll(data.contentList.map { model ->
                    UserInfoImageListItem(
                        model = model as SelectUserMakeBoardModelModel,
                        interaction = this
                    )
                })
            }

            BottomSheetType.SELECT_STAMP_BOARD -> {
                items.addAll(data.contentList.map { model ->
                    UserInfoListItem(
                        model = model as SelectUserStampBoardModel,
                        interaction = this
                    )
                })
            }
        }

        this.adapter.updateItem(item = items)
    }

    fun closeBottomSheet() {
        dismiss()
    }

    fun businessBottomSheet() {
        onClickListener?.let {
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

    private fun selectItem(items: List<BindableItem<*>>, modelId: Int): BindableItem<*>? {
        var selectedItem: BindableItem<*>? = null
        items.forEach { item ->
            when (item) {
                is MissionListItem -> {
                    item.isSelected = item.model.id == modelId
                    if (item.isSelected) selectedItem = item
                }
                is UserInfoImageListItem -> {
                    item.isSelected = item.model.userId == modelId
                    if (item.isSelected) selectedItem = item
                }
                is UserInfoListItem -> {
                    item.isSelected = item.model.userId == modelId
                    if (item.isSelected) selectedItem = item
                }
            }
        }
        return selectedItem
    }

    /**
     * 미션 : BottomSheetType.MISSION
     */
    override fun onMissionClick(model: MissionModel) {
        selectItem(items, model.id)?.let {
            adapter.notifyDataSetChanged()
            this.returnValue = model
            binding.bottomSheetPositiveButton.isEnabled = true
        }
    }

    /**
     * 프로필 조회 : BottomSheetType.PROFILE_IMAGE
     */
    override fun onUserProfileClick(model: SelectUserMakeBoardModelModel) {
        selectItem(items, model.userId)?.let {
            adapter.notifyDataSetChanged()
            this.returnValue = model
            binding.bottomSheetPositiveButton.isEnabled = true
        }
    }

    /**
     * 도장판 조회 : BottomSheetType.SELECT_STAMP_BOARD
     */
    override fun onUserClick(model: SelectUserStampBoardModel) {
        selectItem(items, model.userId)?.let {
            adapter.notifyDataSetChanged()
            this.returnValue = model
            binding.bottomSheetPositiveButton.isEnabled = true
            businessBottomSheet()
        }
    }

}