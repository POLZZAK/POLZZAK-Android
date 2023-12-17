package com.polzzak_android.presentation.component.newbottomsheet.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.polzzak_android.R
import com.polzzak_android.databinding.BottomsheetBaseBinding

/**
 * 바텀시트의 가장 Base가 되는 클래스
 */
abstract class BaseBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetBaseBinding? = null
    protected val baseBinding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 바텀시트 내용이 커도 한번에 펼쳐지도록, 중간에 걸리지 않도록 설정
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialogInterface ->
            val _dialog = dialogInterface as BottomSheetDialog

            _dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.also {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isDraggable = false
                }

            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_base, container, false)
        return baseBinding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
}