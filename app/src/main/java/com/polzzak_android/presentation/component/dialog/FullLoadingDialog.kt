package com.polzzak_android.presentation.component.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.polzzak_android.R
import com.polzzak_android.databinding.DialogFullLoadingOpacityBinding

class FullLoadingDialog : DialogFragment() {
    var message: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return DataBindingUtil.inflate<DialogFullLoadingOpacityBinding?>(
            inflater,
            R.layout.dialog_full_loading_opacity,
            container,
            false
        ).apply {
            if (message.isBlank()) {
                tvMessage.visibility = View.GONE
            } else {
                tvMessage.text = message
                tvMessage.visibility = View.VISIBLE
            }
        }.root

    }

    companion object {
        /**
         * 다이얼로그에 표시할 텍스트를 지정하여 [FullLoadingDialog]의 인스턴스 생성.
         */
        fun messageOf(text: String): FullLoadingDialog = FullLoadingDialog().apply {
            message = text
        }
    }
}