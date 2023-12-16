package com.polzzak_android.presentation.component.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.presentation.common.util.getDeviceSize
import com.polzzak_android.databinding.CommonDialogBinding
import com.polzzak_android.databinding.ItemDialogMissionListBinding
import com.polzzak_android.presentation.common.util.BindableItem
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.common.util.getCurrentDate
import com.polzzak_android.presentation.feature.stamp.model.MissionModel

/**
 * 다이얼로그 공통 코드
 *
 * type의 DialogStypeType으로 바디 출력 형태 구분 (ALERT: 기본형, CALENDAR: 캘린더형, MISSION: 미션형, LOADING: 로딩형, STAMP: 도장형, MISSION_LIST: 미션리스트형)
 *
 * @see CommonDialogModel
 */
class CommonDialogHelper(
    private val content: CommonDialogModel,
    private var onCancelListener: (() -> OnButtonClickListener)? = null,
    private var onConfirmListener: (() -> OnButtonClickListener)? = null,
) : DialogFragment() {

    private var selectedDate: SelectedDateModel = getCurrentDate()

    companion object {
        fun getInstance(
            content: CommonDialogModel,
            onCancelListener: (() -> OnButtonClickListener)? = null,
            onConfirmListener: (() -> OnButtonClickListener)? = null,
        ): CommonDialogHelper {
            return CommonDialogHelper(content, onCancelListener, onConfirmListener)
        }
    }

    private var _binding: CommonDialogBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // type이 '로딩'인 경우, 사용자 터치에 다이얼로그가 사라지지 않음
        if (content.type == DialogStyleType.LOADING) {
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.common_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        onClickListener()
    }

    private fun setData() {
        binding.data = content
        binding.dialogMission.missionData = content.content.mission

        if (content.type == DialogStyleType.CALENDAR) {
            setCalendarDateListener()
        }

        if (content.type == DialogStyleType.STAMP) {
            binding.dialogStamp.setImageResource(content.content.stampImg!!)
        }

        if (content.type == DialogStyleType.MISSION_LIST) {
            binding.dialogMissionListRc.adapter = BindableItemAdapter()

            val missionList = content.content.missionList
            val requestListRecyclerView = binding.dialogMissionListRc
            val adapter = (requestListRecyclerView.adapter as? BindableItemAdapter) ?: return
            val items = mutableListOf<BindableItem<*>>()

            items.addAll(missionList!!.map { model ->
                MissionListItem(
                    isOneList = missionList.size == 1,
                    model = model,
                )
            })

            adapter.updateItem(item = items)
        }

        if (content.type == DialogStyleType.CAPTURE) {
            binding.ivCouponImage.setImageBitmap(content.content.captureBitmap)
        }

        if (content.type == DialogStyleType.MISSION) {
            binding.dialogMission.dialogMissionImg.setImageResource(content.content.mission?.img!!)
        }
    }

    private fun setCalendarDateListener() {
        binding.dialogCalendar.dialogCalendarView.apply {
            minDate = System.currentTimeMillis()
            setOnDateChangeListener { view, year, month, dayOfMonth ->
                selectedDate = selectedDate.copy(year = year, month = month, day = dayOfMonth)
            }
        }
    }

    class MissionListItem(
        private val isOneList: Boolean,
        private val model: MissionModel,
    ) :
        BindableItem<ItemDialogMissionListBinding>() {
        override val layoutRes = R.layout.item_dialog_mission_list
        override fun bind(binding: ItemDialogMissionListBinding, position: Int) {
            with(binding) {
                isOneList = this@MissionListItem.isOneList
                mission = model
            }
        }

        override fun areItemsTheSame(other: BindableItem<*>): Boolean =
            other is MissionListItem && other.model.id == this.model.id

        override fun areContentsTheSame(other: BindableItem<*>): Boolean =
            other is MissionListItem && other.model == this.model
    }

    private fun onClickListener() {
        binding.dialogNegativeButton.setOnClickListener {
            onCancelListener?.invoke()?.setBusinessLogic()
            dismiss()
        }

        binding.dialogPositiveButton.setOnClickListener {
            onConfirmListener?.invoke()?.setBusinessLogic()

            if (content.type == DialogStyleType.CALENDAR) {
                onConfirmListener?.invoke()?.getReturnValue(selectedDate)
            }

            if (content.type == DialogStyleType.CAPTURE) {
                onConfirmListener?.invoke()?.getReturnValue(binding.dialogCaptureFrame.drawToBitmap())
            }
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = getDeviceSize(requireContext()).first
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}