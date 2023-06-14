package com.polzzak_android.presentation.common.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.common.util.getDeviceSize
import com.polzzak_android.databinding.CommonDialogBinding
import com.polzzak_android.presentation.common.model.CommonDialogModel
import com.polzzak_android.presentation.common.model.DialogStyleType

/**
 * 다이얼로그 공통 코드
 *
 * type의 DialogStypeType으로 바디 출력 형태 구분 (ALERT: 기본형, CALENDAR: 캘린더형, MISSION: 미션형)
 *
 * @see CommonDialogModel
 */
class CommonDialogHelper(
    private val content: CommonDialogModel,
    private var onCancelListener: (() -> OnButtonClickListener)? = null,
    private var onConfirmListener: (() -> OnButtonClickListener)? = null,
) : DialogFragment() {

    companion object {
        fun getInstance(
            content: CommonDialogModel,
            onCancelListener: (() -> OnButtonClickListener)? = null,
            onButtonClickListener: (() -> OnButtonClickListener)? = null,
        ): CommonDialogHelper {
            return CommonDialogHelper(content, onCancelListener, onButtonClickListener)
        }
    }

    private var _binding: CommonDialogBinding? = null
    val binding get() = _binding!!

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
    }

    private fun getCalendarDate(): String {
        // todo: api 확정 후 변경
        binding.dialogCalendar.dialogCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
        }

        return "로직 테스트"
    }

    private fun onClickListener() {
        binding.dialogNegativeButton.setOnClickListener {
            onCancelListener?.invoke()?.setBusinessLogic()
            dismiss()
        }

        binding.dialogPositiveButton.setOnClickListener {
            onConfirmListener?.invoke()?.setBusinessLogic()
            if (content.type == DialogStyleType.CALENDAR) {
                val result = getCalendarDate()
                onConfirmListener?.invoke()?.getReturnValue(result)
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

    object SetVisibility {
        @JvmStatic
        @BindingAdapter("setVisibility")
        fun setVisibility(view: View, isVisible: Boolean) {
            if (isVisible) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}