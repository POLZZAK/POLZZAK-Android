package com.polzzak_android.presentation.component.newbottomsheet.issuecoupon

import androidx.core.text.toSpannable
import com.polzzak_android.R
import com.polzzak_android.databinding.BottomsheetIssueCouponBinding
import com.polzzak_android.presentation.common.model.ButtonCount
import com.polzzak_android.presentation.common.model.CommonButtonModel
import com.polzzak_android.presentation.component.dialog.CommonDialogContent
import com.polzzak_android.presentation.component.dialog.CommonDialogHelper
import com.polzzak_android.presentation.component.dialog.CommonDialogModel
import com.polzzak_android.presentation.component.dialog.DialogStyleType
import com.polzzak_android.presentation.component.dialog.OnButtonClickListener
import com.polzzak_android.presentation.component.dialog.SelectedDateModel
import com.polzzak_android.presentation.component.newbottomsheet.base.BaseSingleBottomSheet
import java.time.LocalDate
import java.util.Calendar

class IssueCouponBottomSheet(
    private val title: String,
    private val onIssueCouponClick: (LocalDate) -> Unit
) : BaseSingleBottomSheet<BottomsheetIssueCouponBinding>() {
    override val layoutId: Int
        get() = R.layout.bottomsheet_issue_coupon

    private var selectedDate: LocalDate? = null

    override fun initialize() {
        binding.apply {
            tvRewardTitle.text = title

            tvSelectedDate.setOnClickListener { openCalendar() }
            ivCalendar.setOnClickListener { openCalendar() }
        }

    }

    private fun openCalendar() {
        CommonDialogHelper.getInstance(
            content = CommonDialogModel(
                type = DialogStyleType.CALENDAR,
                content = CommonDialogContent(
                    title = "선물 예정일 설정".toSpannable(),
                    calendar = Calendar.getInstance()
                ),
                button = CommonButtonModel(
                    buttonCount = ButtonCount.TWO,
                    negativeButtonText = "닫기",
                    positiveButtonText = "설정 완료"
                )
            ),
            onConfirmListener = {
                object  : OnButtonClickListener {
                    override fun setBusinessLogic() {

                    }

                    override fun getReturnValue(value: Any) {
                        binding.tvSelectedDate.text = (value as SelectedDateModel).let {
                            // 선택한 날짜 저장
                            selectedDate = LocalDate.of(it.year, it.month, it.day)
                            // 선택한 날짜를 TextView에 표시
                            "${it.year}.${it.month}.${it.day}"
                        }

                        binding.btnIssueCoupon.isEnabled = (selectedDate != null)
                    }
                }
            }
        ).show(childFragmentManager, null)
    }
}