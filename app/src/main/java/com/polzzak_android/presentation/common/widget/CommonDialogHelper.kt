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

/**
 * 다이얼로그 공통 코드
 *
 * type의 DialogStypeType으로 바디 출력 형태 구분 (ALERT: 기본형, CALENDAR: 캘린더형, MISSION: 미션형)
 *
 * @see DialogModel
 *
 * <pre>
 * { @code
 * CommonDialogHelper.newInstance(
 *      content = DialogModel(
 *          type = DialogStyleType.ALERT,
 *          content = DialogContent(
 *              title = "제목",
 *              body = "내용",
 *              mission = DialogMissionContent(
 *                  img = "이미지 link",
 *                  missionTitle = "미션 제목",
 *                  missionTime = "미션 시간"
 *                  ),
 *              calendar = Calendar.getInstance()
 *          ),
 *          button = DialogButton(
 *              buttonCount = ButtonCount.ONE,
 *              negativeButtonText = "취소",
 *              positiveButtonText = "확인")
 *          ),
 *      onCancelListener = {
 *          // 취소 버튼 클릭 로직
 *      },
 *      onConfirmListener = {
 *          // 확인 버튼 클릭 로직
 *      }).show(childFragmentManager, "Dialog")
 * }
 * </pre>
 *
 */
class CommonDialogHelper(
    private val content: DialogModel,
    private var onCancelListener: (() -> Unit)? = null,
    private var onConfirmListener: (() -> Unit)? = null,
) : DialogFragment() {

    companion object {
        fun newInstance(
            content: DialogModel,
            onCancelListener: (() -> Unit)? = null,
            onConfirmListener: (() -> Unit)? = null
        ): CommonDialogHelper {
            return CommonDialogHelper(content, onCancelListener, onConfirmListener)
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

    private fun getCalendarDate() {
        binding.dialogCalendar.dialogCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // todo 캘린더 로직 추가 -> api 확정 후
        }
    }

    private fun onClickListener() {
        binding.dialogNegativeButton.setOnClickListener {
            onCancelListener?.invoke()
            dismiss()
        }

        binding.dialogPositiveButton.setOnClickListener {
            onConfirmListener?.invoke()
            if (content.type == DialogStyleType.CALENDAR) {
                getCalendarDate()
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